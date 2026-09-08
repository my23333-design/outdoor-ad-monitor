// pages/historyList/historyList.js
const { request, BASE_URL } = require('../../utils/request.js')

Page({
  data: {
    typeOptions: [
      { value: null, label: '全部' },
      { value: '1', label: '禁用词类' },
      { value: '2', label: '虚假宣传' },
      { value: '3', label: '低俗类' },
      { value: '4', label: '敏感类' },
      { value: '5', label: '其他类' }
    ],
    violationType: null,
    auditStatus: '0',
    refreshing: false,
    loadingMore: false,
    historyList: [],
    showConfirm: false,
    showDropdown: false,
    currentTypeName: '',
    pageNum: 1,
    pageSize: 10,
    total: 0,
    handleCount: 0,
    userId: "",
    nickName: "",
    username: "",
    roleName: "",
    encodeData: ""
  },

  onShow() {
    this.getData();
  },

  onPullDownRefresh() {
    this.setData({ refreshing: true, pageNum: 1 });
    this.fetchStoppedList(() => {
      wx.stopPullDownRefresh();
      this.setData({ refreshing: false });
    });
  },

  getData() {
    this.setData({
      userId: wx.getStorageSync('userId') || '100',
      nickName: wx.getStorageSync('nickName') || '用户',
      username: wx.getStorageSync('username') || '',
      roleName: wx.getStorageSync('roleName') || ''
    });
    this.fetchStoppedList();
  },

  // 从后端获取叫停广告数据
  fetchStoppedList(callback) {
    wx.showLoading({ title: '加载中...' });
    
    request({
      url: '/api/advertisement/advertisement/stopped/list',
      method: 'GET',
      success: (res) => {
        wx.hideLoading();
        console.log('从后端获取的叫停广告数据:', res);
        
        if (res.code === 200 && res.data) {
          const stoppedList = res.data;
          console.log('处理前的列表数据:', stoppedList);
          
          // 保存到本地缓存
          wx.setStorageSync('enforcementData', stoppedList);
          
          this.processAndDisplayData(stoppedList);
        } else {
          console.log('请求失败，使用本地缓存');
          // 如果请求失败，尝试从本地缓存读取
          const cacheData = wx.getStorageSync('enforcementData') || [];
          if (cacheData.length > 0) {
            this.processAndDisplayData(cacheData);
          } else {
            this.setData({
              historyList: [],
              total: 0,
              handleCount: 0
            });
          }
        }
        
        if (callback) callback();
      },
      fail: (err) => {
        wx.hideLoading();
        console.error('请求失败:', err);
        // 请求失败，使用本地缓存
        const cacheData = wx.getStorageSync('enforcementData') || [];
        if (cacheData.length > 0) {
          this.processAndDisplayData(cacheData);
        } else {
          this.setData({
            historyList: [],
            total: 0,
            handleCount: 0
          });
        }
        if (callback) callback();
      }
    });
  },

  // 处理图片路径（完整支持JSON数组和逗号分隔格式）
  processImagePath(path) {
    if (!path) return ''
    
    let cleanedPath = String(path).trim().replace(/^["'\s]+|["'\s]+$/g, '')
    
    // 处理JSON数组格式
    if (cleanedPath.startsWith('[') && cleanedPath.endsWith(']')) {
      try {
        const images = JSON.parse(cleanedPath)
        if (Array.isArray(images) && images.length > 0) {
          return this.processImagePath(images[0])
        }
      } catch (e) {
        console.error('解析图片JSON数组失败:', e, '原始路径:', path)
      }
    }
    
    // 处理逗号分隔的多路径
    if (cleanedPath.includes(',') && !cleanedPath.startsWith('http')) {
      const firstPath = cleanedPath.split(',')[0].trim()
      return this.processImagePath(firstPath)
    }
    
    // 已经是完整URL
    if (cleanedPath.startsWith('http://') || cleanedPath.startsWith('https://')) {
      return cleanedPath
    }
    
    // 相对路径，拼接BASE_URL
    if (cleanedPath.startsWith('/')) {
      return BASE_URL + cleanedPath
    }
    
    // 其他情况，直接拼接BASE_URL
    return BASE_URL + '/' + cleanedPath
  },

  // 处理并展示数据
  processAndDisplayData(allData) {
    console.log('开始处理数据，原始数据:', allData);
    // 确保数据有完整的字段
    const processedData = allData.map(item => {
      const processedItem = {
        ...item,
        adImages: this.processImagePath(item.adImages),
        beforeImages: item.beforeImages || (item.adImages ? [this.processImagePath(item.adImages)] : []),
        afterImages: item.afterImages ? this.processImagePath(item.afterImages) : '',
        // 确保地址字段有值
        city: item.city || '',
        district: item.district || '',
        street: item.street || '',
        address: item.address || ''
      };
      console.log('处理后的广告项:', processedItem);
      return processedItem;
    });
    
    console.log('处理完成的数据:', processedData);
    
    // 根据状态筛选
    let filteredData = processedData;
    if (this.data.auditStatus === '0') {
      // 待处理：没有处理结果或处理结果为空
      filteredData = processedData.filter(item => !item.processResult);
    } else if (this.data.auditStatus === '1') {
      // 已处理：有处理结果
      filteredData = processedData.filter(item => item.processResult);
    }
    
    // 行业分类筛选
    if (this.data.violationType) {
      filteredData = filteredData.filter(item => item.violationType === this.data.violationType);
    }
    
    // 计算统计数据
    const total = processedData.length;
    const handleCount = processedData.filter(item => item.processResult).length;
    
    // 分页
    const start = (this.data.pageNum - 1) * this.data.pageSize;
    const end = start + this.data.pageSize;
    const pagedData = filteredData.slice(start, end);
    
    console.log('最终显示的数据:', pagedData);
    
    this.setData({
      historyList: this.data.pageNum === 1 ? pagedData : [...this.data.historyList, ...pagedData],
      total: total,
      handleCount: handleCount
    });
  },

  onClickAvatar() {
    this.setData({ showConfirm: true });
  },

  closeDialog() {
    this.setData({ showConfirm: false });
  },

  confirmQuit() {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userId');
    wx.removeStorageSync('nickName');
    wx.removeStorageSync('username');
    wx.removeStorageSync('roleKey');
    wx.removeStorageSync('roleName');

    wx.reLaunch({ url: '/pages/login/login' });
  },

  onReachBottom() {
    if (this.data.loadingMore) return;
    if (this.data.pageNum * this.data.pageSize >= this.data.total) return;
    
    this.setData({ loadingMore: true });
    
    setTimeout(() => {
      const nextPage = this.data.pageNum + 1;
      const cacheData = wx.getStorageSync('enforcementData') || [];
      const start = (nextPage - 1) * this.data.pageSize;
      const end = start + this.data.pageSize;
      const pagedData = cacheData.slice(start, end);
      
      this.setData({ 
        pageNum: nextPage, 
        historyList: [...this.data.historyList, ...pagedData], 
        loadingMore: false 
      });
    }, 1000);
  },

  toggleDropdown() {
    this.setData({ showDropdown: !this.data.showDropdown });
  },

  selectType(e) {
    const value = e.currentTarget.dataset.value || null;
    const label = e.currentTarget.dataset.label || '全部';
    const actualValue = value === '' ? null : value;
    const cacheData = wx.getStorageSync('enforcementData') || [];
    
    this.setData({ 
      violationType: actualValue, 
      currentTypeName: label, 
      showDropdown: false, 
      pageNum: 1 
    }, () => {
      this.processAndDisplayData(cacheData);
    });
  },

  stopPropagation() {
    // 阻止事件冒泡
  },

  onStatusChange(e) {
    const status = e.currentTarget.dataset.status;
    const cacheData = wx.getStorageSync('enforcementData') || [];
    
    this.setData({ auditStatus: status, pageNum: 1 }, () => {
      this.processAndDisplayData(cacheData);
    });
  }
});