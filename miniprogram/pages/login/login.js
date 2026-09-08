// pages/login/login.js

Page({
  data: {
    username: "",
    password: "",
    roleKey: "collect", // collect=数据采集者, handle=执法处理者
    isVerifying: false
  },

  onLoad() {
    // 清除旧的登录态，重新选择角色
    wx.removeStorageSync('token')
  },

  onRoleSelect(e) {
    const roleKey = e.currentTarget.dataset.roleKey;
    this.setData({ roleKey: roleKey });
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onSubmit() {
    const { username, password, roleKey, isVerifying } = this.data;
    
    if (isVerifying) return;

    if (!username || !password) {
      wx.showToast({ title: '请填写账号和密码', icon: 'none' });
      return;
    }
  
    this.setData({ isVerifying: true });
    wx.showLoading({ title: '登录中...' });
  
    setTimeout(() => {
      wx.hideLoading();
      this.setData({ isVerifying: false });
      
      const roleName = roleKey === 'handle' ? '执法处理者' : '数据采集者';
      wx.setStorageSync('token', 'mock-token-' + Date.now());
      wx.setStorageSync('username', username);
      wx.setStorageSync('userId', '1');
      wx.setStorageSync('nickName', username);
      wx.setStorageSync('roleKey', roleKey);
      wx.setStorageSync('roleName', roleName);
      
      wx.showToast({ title: '登录成功', icon: 'success' });
      
      setTimeout(() => {
        wx.reLaunch({ url: '/pages/index/index' });
      }, 1500);
    }, 1000);
  }
})
