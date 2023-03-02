<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                    <label for="username" class="form-label">User name:</label>
                    <input v-model="username" type="text" id="username" class="form-control" placeholder="enter user name here..">
                    </div>

                    <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input v-model="password" type="password" id="password" class="form-control" placeholder="enter password here..">
                    </div>
                    
                    <div class="error-message">{{error_message}}</div>

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </ContentField>

</template>

<script>

import ContentField from "@/components/ContentField.vue"
import {useStore} from "vuex"
import {ref} from "vue"
import router from "@/router/index"

export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const login = ()=>{
            error_message.value = "";
            store.dispatch("login",{
                username : username.value,
                password: password.value,
                success(){
                    store.dispatch("getinfo",{
                        success(){
                            router.push({name:"home"});
                            console.log(store.state.user);
                        }
                    });
                    
                },
                error(resp){
                    error_message.value = "user name or password is wrong"
                    console.log(resp);
                }

            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>

button {
    width: 100%;
}

div.error-message{
    color: red;
}
</style>