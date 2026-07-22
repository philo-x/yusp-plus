/**
 * @created by tuxw 2020-01-06
 * @updated by
 * @description 浏览器指纹获取
 */
import Fingerprint2 from 'fingerprintjs2'
export function getTFP () {
  const tfpId = localStorage.getItem('tfpId');
  if (!tfpId) {
    Fingerprint2.get(function(components) {
      const values = components.map(function(component, index) {
        return component.value;
      })
      const tfpId = Fingerprint2.x64hash128(values.join(''), 31);
      localStorage.setItem('tfpId', tfpId);
    });
  }
  return tfpId;
};