import $ from "jquery"

export default ({
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state,user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token){
            state.token = token;
        },
        logout(state){
            state.id ="";
            state.username ="";
            state.photo = "";
            state.token = "";
            state.is_login = false;
        }
        
    },
    actions: {
        login(context, data){
            $.ajax({
                url:"https://app5070.acapp.acwing.com.cn/api/user/account/token/",
                type:"post",
                contentType: "application/json",
                data:JSON.stringify({
                  username:data.username,
                  password:data.password
                }),
                success(resp){
                    if(resp.error_message === "success"){
                        localStorage.setItem("jwt_token_sb",resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp){
                  data.error(resp);
                  localStorage.removeItem("jwt_token_sb");
                    context.commit("logout");
                    console.log("sth error!");
                }
              });
        },
        getinfo(context, data){
            $.ajax({
                url:"https://app5070.acapp.acwing.com.cn/api/user/account/info/",
                type:"get",
                contentType: "application/json",
                headers:{
                  Authorization : "Bearer " + context.state.token,
                },
                success(resp){
                    if(resp.error_message === "success"){
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true,
                          });
                          data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                statusCode:{
                    500:function(){
                        // if jwt expired, it will get 500
                        localStorage.removeItem("jwt_token_sb");
                        context.commit("logout");
                    }
                },
                error(resp){
                  data.error(resp);
                  // if other error, logout as well.
                  localStorage.removeItem("jwt_token_sb");
                  context.commit("logout");
                }
              });
        },
        logout(context){
            localStorage.removeItem("jwt_token_sb");
            context.commit("logout");
        }

    },
    modules: {
    }
  })