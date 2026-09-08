// 导入基础URL配置
import {baseURL} from "./apiUrl"

/**
 * 格式化日期时间
 * @param {Date} date - 日期对象
 * @returns {string} 格式化后的日期时间字符串，格式为：年/月/日 时:分:秒
 */
const formatTime = date => {
  const year = date.getFullYear() // 获取年份
  const month = date.getMonth() + 1 // 获取月份（注意：月份从0开始，需要+1）
  const day = date.getDate() // 获取日期
  const hour = date.getHours() // 获取小时
  const minute = date.getMinutes() // 获取分钟
  const second = date.getSeconds() // 获取秒

  // 使用formatNumber格式化数字并拼接成完整时间字符串
  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}

/**
 * 格式化数字，不足两位前面补0
 * @param {number|string} n - 需要格式化的数字
 * @returns {string} 格式化后的字符串
 */
const formatNumber = n => {
  n = n.toString() // 转换为字符串
  // 如果是两位数直接返回，否则前面补0
  return n[1] ? n : `0${n}`
}

/**
 * 处理图片URL
 * @param {string} url - 图片URL字符串，可能包含多个URL用逗号分隔
 * @returns {string} 处理后的图片URL
 */
// 处理图片URL
export const handleImageUrl = (url) => {
  // 如果url为空或无效，返回默认图片
  if (!url) {
    return '/static/images/approval.png';
  }
  
  // 分割多张图片URL，取第一张
  const urls = url.split(',');
  const firstUrl = urls[0].trim();
  
  // 如果是完整HTTP URL直接返回
  if (firstUrl.startsWith('http')) {
    return firstUrl;
  }
  
  // 如果是相对路径，拼接基础URL
  if (firstUrl.startsWith('/profile')) {
    // return `${baseURL}${firstUrl}`;
    return '/static/images/approval.png';

  }
  
  // 其他情况返回默认图片
  return '/static/images/approval.png';
}

// 导出工具函数
export default {
  formatTime, // 时间格式化函数
  handleImageUrl // 图片URL处理函数
}