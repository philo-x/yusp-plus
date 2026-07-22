<!--  -->
<template>
  <div class="excelSyn">
    <yufp-excel-export :exportUrl="excelExportUrl" :exportParam="exportParam"></yufp-excel-export>
    <yufp-excel-import :uploadfileUrl="excelImportUrl" :downloadUrl="downloadUrl" @successImport="successImport"></yufp-excel-import>
    <yu-single-upload :headers="uploadHeader" :action="singleFileUploadUrl" :file="fileListInfo" :upload-text="uploadText" @uploaded="uploadedFn" @delete="deleteFileFn" @load-number="loadNumberFn">
    </yu-single-upload>
  </div>
</template>

<script>
import YufpExcelExport from '@/components/widgets/YufpExcelExport';
import YufpExcelImport from '@/components/widgets/YufpExcelImport';
import YuSingleUpload from '@/components/widgets/YuSingleUpload';
// import { getAccessToken} from '@/utils/oauth';
export default {
  components: {YufpExcelExport, YufpExcelImport, YuSingleUpload },
  data() {
    return {
      excelImportUrl: backend.appOcaService + '/api/studentscore/asyncimport/batch', // 导入url
      excelExportUrl: backend.appOcaService + '/api/studentscore/asyncexport/normal', //导出url
      downloadUrl: backend.appOcaService + '/api/studentscore/template', //模板下载
      exportParam: { //文件导出参数
        userName: '', //姓名
        logIds: [] //这个参数是因为组件里边有用到，所以申明一下，如果选择列表数据导出，则将选中的值放到里边
      },
      singleFileUploadUrl: backend.appOcaService + '/api/file/provider/uploadfile',
      fileListInfo: [],
      uploadText: "单个附件10MB以内，最多10个附件",
      loadFileNum: 0,
      fileList: [],
      // uploadHeader: {Authorization: 'Bearer ' + getAccessToken()},
    };
  },
  methods: {
    uploadedFn(fileItem, num) {
      fileItem.icon && delete fileItem.icon;
      this.fileList.push(fileItem);
    },
    deleteFileFn(file) {
      this.fileList.forEach((item, index) => {
        if(item.filePath === file.filePath) {
          this.fileList.splice(index, 1);
        }
      })
    },
    loadNumberFn(val) {
      this.loadFileNum = val;
    },
  },
};
</script>
<style scoped>
</style>