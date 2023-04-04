import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound"
import PkindexView from "@/views/pk/PkIndexView";
import RankListindexView from "@/views/ranklist/RankListIndexView";
import RecordindexView from "@/views/record/RecordIndexView";
import UserBotindexView from "@/views/user/bot/UserBotIndexView";
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView";
import RecordContentView from "@/views/record/RecordContentView"
import store from "@/store/index";

const routes = [
  {
    path:"/",
    name:"home",
    redirect:"/pk/",
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/404/",
    name:"404",
    component: NotFound,
    meta:{
      requestAuth : false
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component: PkindexView,
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component: RankListindexView,
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component: RecordindexView,
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/record/:recordId",
    name:"record_content",
    component: RecordContentView,
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component: UserBotindexView,
    meta:{
      requestAuth : true
    }
  },
  {
    path:"/user/account/login",
    name:"user_account_login",
    component: UserAccountLoginView,
    meta:{
      requestAuth : false
    }
  },
  {
    path:"/user/account/register",
    name:"user_account_register",
    component: UserAccountRegisterView,
    meta:{
      requestAuth : false
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from,next)=>{
  //localStorage.removeItem("jwt_token_sb");
  const jwt_token = localStorage.getItem("jwt_token_sb");
 
  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    
    store.dispatch("getinfo", {
      success() {
       
      },
      error() {
        
        router.push({ name: 'user_account_login' });
      }
    })
    if(to.name==="user_account_login"){
      router.push({name:"home"});
    }
    next();
  } else {

    if (to.meta.requestAuth && !store.state.user.is_login) {
      next({name: "user_account_login"});
    } else {
      next();
    }
    
  }

})

export default router
