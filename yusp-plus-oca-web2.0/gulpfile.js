const path = require('path')
const gulp = require('gulp')
const cleanCSS = require('gulp-clean-css');
const cssWrap = require('gulp-css-wrap');
const rename = require('gulp-rename');  
const themes = [
  // 默认是紫色,皮肤id需要和主题中皮肤目录相同对应
  { id: 'blue', color: '#2877FF', name: '蓝色（默认）' },
  { id: 'orange', color: '#FB8D12', name: '橙色' },
  { id: 'purple', color: '#5557b9', name: '紫色' }
];
const tasks = [];
themes.forEach(item => {
  const taskName = `css-wrap-${item.id}`
  gulp.task(taskName, function() {
    return gulp.src(path.resolve(`./src/assets/${item.id}/main.css`))
      .pipe(cssWrap({selector: `.${item.id}`}))
      .pipe(cleanCSS())
      .pipe(rename({
        basename: 'dist',	// 文件名
        suffix: '.min',	// 文件名后缀
        extname: '.css'		// 文件扩展名
      }))
      .pipe(gulp.dest(`./src/assets/${item.id}`));
  });
  tasks.push(taskName);
})

console.log('当前处理皮肤任务列表', tasks);
if (tasks.length > 0) {
  gulp.task('default',gulp.series(tasks, done => {
    console.log('任务完成');
    done()
  }));
} else {
  console.warning('没有要处理的任务');
}
