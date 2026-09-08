// index.js
// 首页页面逻辑文件

// 创建页面实例
Page({
  /**
   * 页面的初始数据
   */
  data: {
    isLoggedIn: false,
    userInfo: null,
    nickName: '',
    roleName: '',
    nickNameText: '',
    stats: {
      uploadCount: 0,
      pendingCount: 0,
      processedCount: 0
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.loadStats();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    this.checkLoginStatus();
    this.loadStats();
  },

  /**
   * 检查登录状态
   */
  checkLoginStatus: function() {
    const token = wx.getStorageSync('token');
    const username = wx.getStorageSync('username');
    const roleKey = wx.getStorageSync('roleKey');
    const roleName = wx.getStorageSync('roleName');
    
    if (!token || !username) {
      wx.showToast({
        title: '请先登录',
        icon: 'none',
        duration: 1500
      });
      
      setTimeout(() => {
        wx.reLaunch({
          url: '/pages/login/login'
        });
      }, 1500);
      return;
    }
    
    this.setData({
      isLoggedIn: true,
      nickName: username,
      roleName: roleName || '',
      nickNameText: username ? username.slice(0, 1) : '用'
    });
  },

  /**
   * 加载统计数据
   */
  loadStats: function() {
    this.setData({
      stats: {
        uploadCount: 12,
        pendingCount: 3,
        processedCount: 8
      }
    });
  },

  /**
   * 跳转到上传页面
   */
  goToUpload: function() {
    wx.navigateTo({
      url: '/pages/upload/upload'
    });
  },

  /**
   * 跳转到历史记录页面
   */
  goToHistory: function() {
    wx.navigateTo({
      url: '/pages/historyList/historyList'
    });
  },

  /**
   * 跳转到统计页面
   */
  goToStatistics: function() {
    wx.showToast({
      title: '统计功能开发中',
      icon: 'none'
    });
  },

  /**
   * 跳转到个人信息页面
   */
  goToProfile: function() {
    wx.showToast({
      title: '个人信息开发中',
      icon: 'none'
    });
  },

  /**
   * 退出登录
   */
  logout: function() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
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
})