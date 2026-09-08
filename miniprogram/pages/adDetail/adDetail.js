// pages/adDetail/adDetail.js
const { request, BASE_URL } = require('../../utils/request.js')

Page({
  data: {
    adData: {},
    beforeImages: [],
    industryList: [],
    mediumList: [],
    afterImages: [],
    violationTypeList: [
      { code: '1', name: '禁用词类' },
      { code: '2', name: '虚假宣传' },
      { code: '3', name: '低俗类' },
      { code: '4', name: '敏感类' },
      { code: '5', name: '其他类' }
    ],
    industryName: '',
    mediumName: '',
    violationTypeName: '',
    processResultList: [
      { code: '0', name: '未处理' },
      { code: '1', name: '已拆除' },
      { code: '2', name: '已罚款' }
    ],
    selectedProcessResult: '',
    selectedProcessResultName: '',
    processPerson: '',
    processResultVisible: false,
    submitting: false,
    showDialog: false,
    dialogContent: '',
    isLawEnforcer: false,
    currentRoleName: ''
  },

  onLoad(options) {
    // 根据登录角色判断是否为执法处理者
    const roleKey = wx.getStorageSync('roleKey')
    const roleName = wx.getStorageSync('roleName') || ''
    const isLawEnforcer = roleKey === 'handle'
    this.setData({ isLawEnforcer, currentRoleName: roleName })

    // 加载行业分类和媒体类型数据
    this.loadIndustryList()
    this.loadMediumList()
    
    if (options.id) {
      const id = parseInt(options.id)
      this.fetchAdDetail(id)
    }
  },

  // 清理字符串中的特殊字符（与upload页面一致）
  cleanText(text) {
    if (!text) return ''
    return text.toString()
      .replace(/\\"/g, '')
      .replace(/^\*+|\*+$/g, '')
      .trim()
  },

  // 从后端获取行业分类列表（与upload页面一致）
  loadIndustryList() {
    this.setData({ industryList: [] })
    request({
      url: '/api/advertisement/advertisement/getIndustryList',
      method: 'GET',
      timeout: 15000,
      success: (res) => {
        console.log('详情页获取行业分类原始响应:', JSON.stringify(res).slice(0, 500))
        let list = null
        if (Array.isArray(res)) list = res
        else if (res && Array.isArray(res.data)) list = res.data
        else if (res && res.data && Array.isArray(res.data.data)) list = res.data.data
        if (list && list.length > 0) {
          const industryList = list.map(item => {
            if (item && typeof item === 'object' && item.name) {
              return this.cleanText(item.name)
            }
            if (item && typeof item === 'object' && item.code) {
              return this.cleanText(item.code)
            }
            return this.cleanText(item)
          }).filter(item => item)
          this.setData({ industryList: industryList })
          console.log('✅ 详情页行业分类列表:', industryList)
          // 行业分类加载完成后更新显示
          this.updateIndustryName()
        } else {
          console.warn('⚠️ 详情页行业分类为空')
        }
      },
      fail: (err) => {
        console.error('❌ 获取行业分类失败:', err)
      }
    })
  },

  // 从后端获取媒体类型列表（与upload页面一致）
  loadMediumList() {
    this.setData({ mediumList: [] })
    request({
      url: '/api/advertisement/advertisement/getMediumList',
      method: 'GET',
      timeout: 15000,
      success: (res) => {
        console.log('详情页获取媒体类型原始响应:', JSON.stringify(res).slice(0, 500))
        let list = null
        if (Array.isArray(res)) list = res
        else if (res && Array.isArray(res.data)) list = res.data
        else if (res && res.data && Array.isArray(res.data.data)) list = res.data.data
        if (list && list.length > 0) {
          const mediumList = list.map(item => {
            if (item && typeof item === 'object' && item.name) {
              return this.cleanText(item.name)
            }
            if (item && typeof item === 'object' && item.code) {
              return this.cleanText(item.code)
            }
            return this.cleanText(item)
          }).filter(item => item)
          this.setData({ mediumList: mediumList })
          console.log('✅ 详情页媒体类型列表:', mediumList)
          // 媒体类型加载完成后更新显示
          this.updateMediumName()
        } else {
          console.warn('⚠️ 详情页媒体类型为空')
        }
      },
      fail: (err) => {
        console.error('❌ 获取媒体类型失败:', err)
      }
    })
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

    // 处理以逗号分隔的多路径
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

  // 处理多张图片，返回图片URL数组
  processImagesToArray(imagesStr) {
    if (!imagesStr) return []

    const cleaned = String(imagesStr).trim().replace(/^["'\s]+|["'\s]+$/g, '')

    // 尝试解析JSON数组
    if (cleaned.startsWith('[') && cleaned.endsWith(']')) {
      try {
        const images = JSON.parse(cleaned)
        if (Array.isArray(images) && images.length > 0) {
          return images.map(img => this.processImagePath(img)).filter(url => url)
        }
      } catch (e) {
        console.error('解析图片数组失败:', e)
      }
    }

    // 尝试按逗号分割
    if (cleaned.includes(',')) {
      return cleaned.split(',').map(p => this.processImagePath(p.trim())).filter(url => url)
    }

    // 单张图片
    const single = this.processImagePath(cleaned)
    return single ? [single] : []
  },

  // 根据行业分类值获取显示名称
  getIndustryName(code) {
    if (!code) return '-'
    const codeStr = String(code).trim()

    // 如果在行业分类列表中，直接显示（列表存的是名称字符串）
    if (this.data.industryList.length > 0) {
      const found = this.data.industryList.find(item => String(item).trim() === codeStr)
      if (found) return found
    }

    // 否则直接显示原始值（可能就是名称）
    return codeStr
  },

  // 根据媒体类型值获取显示名称
  getMediumName(code) {
    if (!code) return '-'
    const codeStr = String(code).trim()

    if (this.data.mediumList.length > 0) {
      const found = this.data.mediumList.find(item => String(item).trim() === codeStr)
      if (found) return found
    }

    return codeStr
  },

  // 根据违法类别code获取名称
  getViolationTypeName(code) {
    if (!code) return '-'
    const codeStr = String(code).trim()
    const found = this.data.violationTypeList.find(item => String(item.code).trim() === codeStr)
    if (found) return found.name
    return codeStr
  },

  // 更新行业分类显示名称
  updateIndustryName() {
    if (this.data.adData && this.data.adData.adIndustryType) {
      const name = this.getIndustryName(this.data.adData.adIndustryType)
      this.setData({ industryName: name })
    }
  },

  // 更新媒体类型显示名称
  updateMediumName() {
    if (this.data.adData && this.data.adData.adMediumType) {
      const name = this.getMediumName(this.data.adData.adMediumType)
      this.setData({ mediumName: name })
    }
  },

  fetchAdDetail(id) {
    wx.showLoading({ title: '加载中...' })

    const enforcementData = wx.getStorageSync('enforcementData') || []
    const adData = enforcementData.find(item => item.id == id)

    if (adData) {
      console.log('🔍 详情页原始广告数据:', adData)
      console.log('🔍 行业分类原始值:', adData.adIndustryType)
      console.log('🔍 媒体类型原始值:', adData.adMediumType)
      console.log('🔍 违法类别原始值:', adData.violationType)
      console.log('🔍 图片原始值:', adData.adImages)
      console.log('🔍 处理后图片原始值:', adData.afterImages)
      console.log('🔍 处理前图片数组原始值:', adData.beforeImages)

      // 处理处理前图片 - 优先使用adImages，支持JSON数组
      let beforeImages = []
      if (adData.beforeImages && Array.isArray(adData.beforeImages) && adData.beforeImages.length > 0) {
        // beforeImages已是数组
        beforeImages = adData.beforeImages.map(img => this.processImagePath(img)).filter(url => url)
      }
      if (beforeImages.length === 0 && adData.adImages) {
        // 从adImages解析图片
        beforeImages = this.processImagesToArray(adData.adImages)
      }
      console.log('✅ 处理后beforeImages数组:', beforeImages)

      // 处理处理后图片
      let afterImages = []
      if (adData.afterImages) {
        afterImages = this.processImagesToArray(adData.afterImages)
      }
      console.log('✅ 处理后afterImages数组:', afterImages)

      // 计算显示名称
      const industryName = this.getIndustryName(adData.adIndustryType)
      const mediumName = this.getMediumName(adData.adMediumType)
      const violationTypeName = this.getViolationTypeName(adData.violationType)
      console.log('✅ 行业分类显示:', industryName)
      console.log('✅ 媒体类型显示:', mediumName)
      console.log('✅ 违法类别显示:', violationTypeName)

      this.setData({
        adData: adData,
        beforeImages: beforeImages,
        afterImages: afterImages,
        industryName: industryName,
        mediumName: mediumName,
        violationTypeName: violationTypeName,
        selectedProcessResult: adData.processResult || '',
        selectedProcessResultName: adData.processResultName || '',
        processPerson: adData.processPerson || ''
      })
      wx.hideLoading()
    } else {
      wx.hideLoading()
      this.showToast('广告数据不存在')
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }
  },

  goBack() {
    wx.navigateBack({
      fail: () => {
        wx.switchTab({
          url: '/pages/historyList/historyList'
        })
      }
    })
  },

  previewImage(e) {
    const { images, index } = e.currentTarget.dataset
    wx.previewImage({
      current: images[index],
      urls: images
    })
  },

  // 选择处理后图片
  chooseAfterImage() {
    if (this.data.afterImages.length >= 9) {
      wx.showToast({ title: '最多上传9张', icon: 'none' })
      return
    }
    wx.chooseImage({
      count: 9 - this.data.afterImages.length,
      success: (res) => {
        console.log('选择处理后图片成功:', res.tempFilePaths)
        const newPhotos = [...this.data.afterImages, ...res.tempFilePaths]
        this.setData({ afterImages: newPhotos })
      }
    })
  },

  // 删除处理后图片
  deleteAfterImage(e) {
    const index = e.currentTarget.dataset.index
    const newPhotos = this.data.afterImages.filter((_, i) => i !== index)
    this.setData({ afterImages: newPhotos })
  },

  // 处理结果选择
  selectProcessResult(e) {
    const { item } = e.currentTarget.dataset
    this.setData({ 
      selectedProcessResult: item.code, 
      selectedProcessResultName: item.name, 
      processResultVisible: false 
    })
  },

  showProcessResult() {
    this.setData({ processResultVisible: true })
  },

  cancelProcessResult() {
    this.setData({ processResultVisible: false })
  },

  // 处理人输入
  onProcessPersonChange(e) {
    this.setData({ processPerson: e.detail.value })
  },

  // 上传单张图片到服务器
  uploadImage(filePath) {
    return new Promise((resolve, reject) => {
      wx.uploadFile({
        url: BASE_URL + '/api/common/upload',
        filePath: filePath,
        name: 'file',
        success: (res) => {
          console.log('图片上传成功，响应:', res)
          try {
            const data = JSON.parse(res.data)
            if (data.code === 200 && data.url) {
              console.log('服务器返回的图片URL:', data.url)
              if (data.fileName) {
                console.log('使用后端返回的fileName:', data.fileName)
                resolve(data.fileName)
              } else if (data.url.includes('/profile/')) {
                const relativePath = data.url.substring(data.url.indexOf('/profile/'))
                resolve(relativePath)
              } else {
                resolve(data.url)
              }
            } else if (data.msg && data.msg.includes('\\')) {
              const localPath = data.msg
              const fileName = localPath.split('\\').pop()
              const url = '/profile/upload/images/' + fileName
              console.log('从本地路径构建URL:', url)
              resolve(url)
            } else {
              console.error('上传失败:', data.msg || data)
              reject(new Error(data.msg || '上传失败'))
            }
          } catch (e) {
            console.error('解析响应失败:', e)
            reject(e)
          }
        },
        fail: (err) => {
          console.error('上传请求失败:', err)
          reject(err)
        }
      })
    })
  },

  // 批量上传图片
  async uploadImages(filePaths) {
    const uploadedUrls = []
    for (const filePath of filePaths) {
      try {
        const url = await this.uploadImage(filePath)
        uploadedUrls.push(url)
      } catch (err) {
        console.error('上传图片失败:', filePath, err)
      }
    }
    return uploadedUrls
  },

  // 提交处理结果
  async submitProcessResult() {
    if (!this.data.selectedProcessResult) {
      this.showToast('请选择处理结果')
      return
    }
    
    if (this.data.submitting) return
    
    this.setData({ submitting: true })
    wx.showLoading({ title: '提交中...' })
    
    try {
      let afterImagesUrl = ''
      
      if (this.data.afterImages.length > 0) {
        console.log('开始上传处理后图片...')
        const uploadedImages = await this.uploadImages(this.data.afterImages)
        console.log('上传成功，图片URLs:', uploadedImages)
        
        if (uploadedImages.length > 0) {
          afterImagesUrl = JSON.stringify(uploadedImages)
        }
      }
      
      const now = new Date()
      const timeStr = now.toISOString().replace('T', ' ').substring(0, 19)
      
      const submitData = {
        id: this.data.adData.id,
        processResult: this.data.selectedProcessResult,
        processResultName: this.data.selectedProcessResultName,
        processPerson: this.data.processPerson,
        processTime: timeStr,
        afterImages: afterImagesUrl
      }
      
      console.log('准备提交处理结果:', submitData)
      
      request({
        url: '/api/advertisement/advertisement/processResult/submit',
        method: 'POST',
        data: submitData,
        success: (res) => {
          console.log('提交成功:', res)
          wx.hideLoading()
          
          const enforcementData = wx.getStorageSync('enforcementData') || []
          const index = enforcementData.findIndex(item => item.id === this.data.adData.id)
          if (index !== -1) {
            enforcementData[index] = {
              ...enforcementData[index],
              ...submitData
            }
            wx.setStorageSync('enforcementData', enforcementData)
          }
          
          this.showToast('提交成功')
          setTimeout(() => {
            this.setData({ submitting: false })
          }, 1500)
        },
        fail: (err) => {
          console.error('提交失败:', err)
          wx.hideLoading()
          this.showToast('提交失败')
          this.setData({ submitting: false })
        }
      })
    } catch (err) {
      console.error('上传过程出错:', err)
      wx.hideLoading()
      this.showToast('图片上传失败')
      this.setData({ submitting: false })
    }
  },

  showToast(message) {
    wx.showToast({
      title: message,
      icon: 'none',
      duration: 2000
    })
  },

  showDialogContent(content) {
    this.setData({
      showDialog: true,
      dialogContent: content
    })
  },

  closeDialog() {
    this.setData({ showDialog: false })
  },

  stopPropagation() {
  }
})
