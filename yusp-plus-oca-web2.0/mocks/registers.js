/**
 * @created by pan
 * @updated by helin3 2019-04-05
 * @description Mock业务模块登记注册表
 * 特别注意：不要试图增加/改变/删除带${XXX}的注释行
 * TODO: 1) 导入你的模拟业务模块；2) 添加你的模拟业务模块
 */
import oauth from './api/common/oauth'
import crud from './api/common/crud'

import article from './api/example/article'
import demo from './api/example/demo'
import subtable from './api/example/subtable'
import maintable from './api/example/maintable'
import concise from './api/example/concise'
import credit from './api/example/credit'
import asynctree from './api/example/asynctree'
import select from './api/example/select'
import uTrace from './api/example/uTrace'

/** ${CliPluginMockApiAutoImport} Dangerous!!! */

export default [
  ...oauth,
  ...crud,
  ...article,
  ...demo,
  ...subtable,
  ...concise,
  ...maintable,
  ...credit,
  ...asynctree,
  ...select,
  ...uTrace,
/** ${CliPluginMockApiAutoExport} Dangerous!!! */
]

