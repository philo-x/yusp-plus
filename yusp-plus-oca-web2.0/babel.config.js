module.exports = {
  presets: [
    ['@vue/app', { useBuiltIns: 'entry' }]
  ],
  sourceType: 'unambiguous',
  "plugins": [
    [
      "@babel/plugin-syntax-dynamic-import"
    ]
  ]
}
