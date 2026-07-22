// 流水线语法请参考Jenkins官方流水线文档：https://www.jenkins.io/doc/book/pipeline
// 本流水线适用于Maven项目的构建与部署，包含了代码拉取、编译打包、发布部署、邮件通知等功能，同时支持多环境的发布部署
// 本流水线使用了Jenkins的Git, Pipeline, Blue Ocean, Node, Publish Over SSH, Email Extension, Email Extension Template Plugin插件，请确保已安装
pipeline {
    // 在任何可用的代理上执行该流水线，需要使用其他节点的语法： agent { node { label "节点名称"}}
    agent any
    // 参数化配置，目前只支持[booleanParam, choice, credentials, file, text, password, run, string]这几种参数类型
    parameters {
        // 代码仓库分支，需修改
        choice(name: 'repoBranch', choices: ['master'], description: 'git分支名称')
        // 部署环境，以英文逗号分割。按修改
        choice(name: 'env', choices: ['dev', 'test', 'uat', 'prod'], description: '部署环境')
        // 是否备份部署的应用
        booleanParam(name: 'isBackupApp', defaultValue: true, description: '是否备份部署的应用')
        // 若勾选，会在pipelie完成后会邮件通知
        booleanParam(name: 'isSendMail', defaultValue: true, description: '是否发送邮件通知')
    }
    // 环境变量，初始确定后一般不需更改，以下配置在Jenkins的全局配置中找到对应的名称配置即可
    tools {
        // node工具，按需修改
        nodejs 'node'
    }
    // 常量参数，按需修改，初始确定后一般不需更改
    environment {
        // -----------------------------------
        // Git服务全系统凭据唯一标识，需修改，以下配置在Jenkins的凭据中找到对应的凭据标识配置即可
        CRED_ID = 'gitlab-open'
        // -----------------------------------
        // Git代码仓库路径，需修改
        REPO_URL = 'http://gitlab.yuxincloud.com:9022/yusp/yusp-plus.git'
        // 编译命令
        COMPILE_CMD = 'cd ${WORKSPACE}/yusp-plus-oca-web2.0 && yarn install && yarn build:theme && yarn build:prod && rm -rf html && mv dist html && zip -r html.zip html'
        // -------------------------------------
        // sshPublisher插件的配置，可参考https://www.jenkins.io/doc/pipeline/steps/publish-over-ssh/
        // sshPublisher配置：jarLocation，构建出的jar包的相对路径(相对WorkSpace)，需修改
        SOURCE_JAR_FILES = 'yusp-plus-oca-web2.0/html.zip'
        //  sshPublisher配置：removePrefix，jar包前面的目录，需修改
        JAR_REMOVE_PREFIX = 'yusp-plus-oca-web2.0'
        // sshPublisher配置：remoteDirectory, 远程部署的目录, 相对于登录用户的家目录，需修改
        REMOTE_DIRECTORY = 'yusp-plus-oca-web2.0'
        // --------------------------------------
        // 邮件通知地址，多个地址以英文逗号分割，需修改
        EMAIL_TO = 'danyb1@yusys.com.cn,xiaodg@yusys.com.cn'
    }
    options {
        // 保持构建的最大个数，按需修改
        buildDiscarder(logRotator(numToKeepStr: '2'))
    }
    // 定时构建
    //triggers {
        // 早上8点构建，按需修改
        //cron('01 09 * * *')
    //}
    // 定义流水线的的阶段
    stages {
        // 从代码仓库拉去代码，无需修改
        stage('拉取代码') {
            steps {
                echo "拉取代码开始：${REPO_URL}"
                git branch: params.repoBranch, credentialsId: CRED_ID, url: REPO_URL
                echo "拉取代码结束"
            }
        }
        //sonar扫描代码
        stage('代码扫描') {
        	steps {
                echo "开始代码扫描"
                sh " cd '${WORKSPACE}/yusp-plus-oca-web2.0' && /root/sonar-scanner-4.6.2.2472-linux/bin/sonar-scanner "
                echo "代码扫描结束"
            }
        }
        // 编译部署
        stage('编译部署') {
            steps {
                script {
                    nodejs(nodeJSInstallationName: 'node', configId: '') {
                        // 执行编译命令
                        echo "开始编译："
                        sh "${COMPILE_CMD}"
                        echo "开始部署："
                        // 所有部署环境的主机列表，key是环境名称，与parameter中的env对应。值是运行主机名称，主机名称从系统配置->Publish over SSH 节点查找
                        def envHosts = [dev: ["dev-139.155.68.146"], test: ["test-192.168.35.31"], uat: [], prod: []]
                        // -------------------------------------------
                        // 获取当前运行环境需要部署的主机
                        def curEnvHosts = envHosts["$params.env"]
                        // 部署命令，可按需调整。注意：以$开头的变量是指获取Jenkins的，以\$开头开头的是获取shell中的
                        def deployCmd = """
                            cd '/root/yusp-plus-oca-web2.0'
                            rm -rf html
                            unzip html.zip
                            echo '部署完成'
                            # 重新运行程序

                            # 备份程序，启动程序后，会将jar包复制一份，名称中加上了日期和时间
                            if [[ "true" == "$params.isBackupApp" ]]
                            then
                                now="\$(date +%Y%m%d)_\$(date +%H%M%S)"
                                cp html.zip 'html_'\$now.zip
                            fi
                         """
                        // 发包部署
                        if (curEnvHosts && curEnvHosts.size() > 0) {
                            curEnvHosts.each {
                                echo "开始部署到主机：$it"
                                sshPublisher(publishers: [sshPublisherDesc(configName: it, transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: deployCmd, execTimeout: 120000, flatten: false, makeEmptyDirs: true, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: "${REMOTE_DIRECTORY}", remoteDirectorySDF: false, removePrefix: "${JAR_REMOVE_PREFIX}", sourceFiles: "${SOURCE_JAR_FILES}")], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                            }
                        } else {
                            def errMsg = "错误：环境[$params.env]没有可部署的主机, 请检查！"
                            echo errMsg
                            error errMsg
                        }
                        echo "编译部署结束"
                    }
                }
            }
        }
    }
}
