// app.js
App({
  onLaunch() {
    try {
      // 展示本地存储能力
      const logs = wx.getStorageSync('logs') || []
      logs.unshift(Date.now())
      wx.setStorageSync('logs', logs)
    } catch (e) {
      console.error('存储日志失败:', e)
    }
  },
  globalData: {
    userInfo: null
  }
})
