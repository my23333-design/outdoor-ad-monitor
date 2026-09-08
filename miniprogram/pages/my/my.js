// pages/my/my.js

Page({
  data: {
    nickName: '',
    roleName: '',
    roleKey: '',
    stats: {
      total: 0,
      pending: 0,
      completed: 0
    }
  },

  onLoad(options) {
  },

  onShow() {
    this.getUserInfo();
    this.getStats();
  },

  getUserInfo() {
    this.setData({
      nickName: wx.getStorageSync('nickName') || '',
      roleName: wx.getStorageSync('roleName') || '',
      roleKey: wx.getStorageSync('roleKey') || ''
    });
  },

  getStats() {
    // 使用模拟数据
    this.setData({
      stats: {
        total: 15,
        pending: 5,
        completed: 10
      }
    });
  },

  goToEnforcementList(e) {
    const status = e.currentTarget.dataset.status || '';
    wx.showToast({
      title: '执法列表功能开发中',
      icon: 'none'
    });
  },

  goToHistory() {
    wx.navigateTo({
      url: '/pages/historyList/historyList'
    });
  },

  goToSettings() {
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    });
  },

  goToAbout() {
    wx.showModal({
      title: '关于',
      content: '户外广告监测系统 v1.0\n\n执法人员专用小程序',
      showCancel: false
    });
  },

  logout() {
    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userId');
          wx.removeStorageSync('nickName');
          wx.removeStorageSync('username');
          wx.removeStorageSync('roleKey');
          wx.removeStorageSync('roleName');
          
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});
