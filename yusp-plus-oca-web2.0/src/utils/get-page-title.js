import defaultSettings from '@/config';
import i18n from '@/locale';

const title = defaultSettings.title || '应用管理框架';

export default function getPageTitle (key) {
  // console.log(`获取页面标题${key}`)
  const hasKey = i18n.te(`route.${key}`);
  if (hasKey) {
    const pageName = i18n.t(`route.${key}`);
    return `${pageName} - ${title}`;
  }
  return `${title}`;
}
