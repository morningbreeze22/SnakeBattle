<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                    <label for="username" class="form-label">User name:</label>
                    <input v-model="username" type="text" id="username" class="form-control" placeholder="enter user name here..">
                    </div>

                    <div class="mb-3">
                    <label for="password" class="form-label">Password:</label>
                    <input v-model="password" type="password" id="password" class="form-control" placeholder="enter password here..">
                    </div>

                    <div class="mb-3">
                    <label for="confirmedPassword" class="form-label">Confirm your Password:</label>
                    <input v-model="confirmedPassword" type="password" id="confirmedPassword" class="form-control" placeholder="enter password again..">
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

import {ref} from "vue"
import $ from "jquery"
import router from "@/router/index"

export default{
    components:{
        ContentField
    },
    setup(){

        let username = ref('');
        let password = ref('');
        let confirmedPassword = ref('');
        let error_message = ref('');

        const register = ()=>{
            $.ajax({
                url:"https://app5070.acapp.acwing.com.cn/api/user/account/register/",
                type:"post",
                contentType: "application/json",
                data:JSON.stringify({
                    username:username.value,
                    password:password.value,
                    confirmedPassword:confirmedPassword.value
                }),
                success(resp){
                    console.log(resp);
                    if(resp.error_message==="success"){
                        alert("Successfully registered! please login");
                        router.push({name:"user_account_login"});
                    }else{
                        error_message.value = resp.error_message;
                    }
                },
                error(resp){
                    console.log(resp);
                    
                }
            });
        }

        return {
            username,
            password,
            error_message,
            confirmedPassword,
            register,
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