<template>
    <div class="matchground">
      <div class="row">
        <div class="col-4">
            <div class="user-photo">
                <img :src="$store.state.user.photo" alt="">
            </div>
            <div class="user-username">
                {{ $store.state.user.username }}
            </div>
        </div>

        <div class="col-4">
            <div class="user-select-bot">
                <select class="form-select" v-model="select_bot" aria-label="Default select example">
                <option selected value="-1">Play manually</option>
                <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                    {{ bot.title }}
                </option>
                
                </select>
            </div>
        </div>

        <div class="col-4">
            <div class="user-photo">
                <img :src="$store.state.pk.opponent_photo" alt="">
            </div>
            <div class="user-username">
                {{ $store.state.pk.opponent_username }}
            </div>
        </div>
        
        <div class="col-12" style="text-align: center; padding-top:15vh">
            <button type="button" class="btn btn-warning btn-lg" @click="click_match_btn">{{match_btn_info}}</button>
        </div>
      </div>
    </div>
</template>

<script>
import { ref} from "vue"
import { useStore } from "vuex";
import $ from "jquery"

    export default {
        setup(){
            const store = useStore();
            let match_btn_info = ref("start!");
            let bots = ref([]);
            let select_bot = ref("-1");

            // change btn text
            const click_match_btn = ()=>{
                if(match_btn_info.value === "start!"){
                    match_btn_info.value = "cancel";
                    store.state.pk.socket.send(JSON.stringify({
                        event: "start-matching",
                        bot_id:select_bot.value
                    }));
                } else{
                    
                    match_btn_info.value = "start!";
                    store.state.pk.socket.send(JSON.stringify({
                        event: "stop-matching",
                    }));
                    
                
                }
            }

 
            // refresh page and fetch all bots from backend
            const refresh_bots = ()=>{
                $.ajax({
                url:"https://app5070.acapp.acwing.com.cn/api/user/bot/getlist/",
                type : "get",
                headers:{
                    Authorization : "Bearer "+ store.state.user.token
                },
                success(resp){
                    bots.value = resp;
                }
            })
            };

            refresh_bots();



            return{
                bots,
                select_bot,
                match_btn_info,
                click_match_btn,
            }
        }       
    }
</script>

<style scoped>
div.matchground{
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50,50,50,0.5);
}
div.user-photo{
    text-align: center;
    padding-top: 10vh;
}

div.user-photo > img{
    border-radius: 50%;
    width: 20vh;
}

div.user-username {
    text-align: center;
    font-size:24px;
    font-weight: 600;
    color: white;
    padding-top: 2vh;
}
div.user-select-bot{
    padding-top: 20vh;
}

div.user-select-bot>select{
    width:60%;
    margin: 0 auto;
}
</style>