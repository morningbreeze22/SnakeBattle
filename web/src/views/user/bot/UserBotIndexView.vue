<template>
    <ContentField>
        my bots
    </ContentField>

</template>

<script>

import ContentField from "@/components/ContentField.vue"
import $ from "jquery"
import { useStore} from "vuex"

export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();

        /*
        $.ajax({
            url:"http://localhost:3000/user/bot/add/",
            type : "post",
            contentType: "application/json",
            data : JSON.stringify({
                title : "my bot",
                description: "description",
                content : "code",
            }),
            headers:{
                Authorization : "Bearer "+ store.state.user.token
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            }
        })
        
*/
        /*
        $.ajax({
            url:"http://localhost:3000/user/bot/remove/",
            type : "post",
            contentType: "application/json",
            data : JSON.stringify({
                bot_id : "1"
            }),
            headers:{
                Authorization : "Bearer "+ store.state.user.token
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            }
        })
        */
        $.ajax({
            url:"http://localhost:3000/user/bot/getlist/",
            type : "get",
            headers:{
                Authorization : "Bearer "+ store.state.user.token
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            }
        })
    }
}
</script>

<style scoped>
</style>