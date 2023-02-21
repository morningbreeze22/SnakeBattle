import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound"
import PkindexView from "@/views/pk/PkIndexView";
import RankListindexView from "@/views/ranklist/RankListIndexView";
import RecordindexView from "@/views/record/RecordIndexView";
import UserBotindexView from "@/views/user/bot/UserBotIndexView";

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/"
  },
  {
    path:"/404/",
    name:"404",
    component: NotFound
  },
  {
    path:"/pk/",
    name:"pk_index",
    component: PkindexView
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component: RankListindexView
  },
  {
    path:"/record/",
    name:"record_index",
    component: RecordindexView
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component: UserBotindexView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
