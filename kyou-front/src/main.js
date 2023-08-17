import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// markdown编辑插件 v-md-editer
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css';
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index';
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css';
import Prism from 'prismjs';


VueMarkdownEditor.use(vuepressTheme, {Prism,});
VueMarkdownEditor.use(createLineNumbertPlugin());
VueMarkdownEditor.use(createHighlightLinesPlugin());
VueMarkdownEditor.use(createEmojiPlugin());

//评论插件
import UndrawUi from 'undraw-ui'
import 'undraw-ui/dist/style.css'

import '@/utils/permission.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$key = "http://localhost:8002";// 定义全局变量
app.use(UndrawUi)
app.use(VueMarkdownEditor);
app.use(router)

app.mount('#app')
