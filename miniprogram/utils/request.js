
const BASE_URL = 'http://localhost:8080'

/**
 * 网络请求封装
 */
function request(options) {
  const { url, method = 'GET', data = {}, timeout = 10000, success, fail, complete } = options

  console.log('请求接口:', BASE_URL + url)
  console.log('请求参数:', data)

  wx.request({
    url: BASE_URL + url,
    method: method,
    data: data,
    timeout: timeout,
    header: {
      'content-type': 'application/json'
    },
    success: (res) => {
      console.log('接口响应:', res)
      if (res.statusCode === 200) {
        if (success) {
          success(res.data)
        }
      } else {
        wx.showToast({
          title: '请求失败',
          icon: 'none'
        })
        if (fail) {
          fail(res)
        }
      }
    },
    fail: (err) => {
      console.error('网络请求错误:', err)
      wx.showToast({
        title: '网络错误',
        icon: 'none'
      })
      if (fail) {
        fail(err)
      }
    },
    complete: complete
  })
}

module.exports = {
  request,
  BASE_URL
}

