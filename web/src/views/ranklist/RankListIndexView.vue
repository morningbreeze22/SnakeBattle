<template>
    <ContentField>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>User</th>
                    <th>Rating</th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="user-photo">
                        &nbsp;
                        <span class ="user-username">{{ user.username }}</span>
                    </td>
                    
                    <td>
                        {{ user.rating }}

                    </td>
                

                </tr>
            </tbody>
        </table>


        <nav aria-label="...">
        <ul class="pagination" style="float:right">
            <li class="page-item">
                <a class="page-link" href="#" @click="click_page(-2)">
                    &lt;
                </a>
            </li>
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number">
                <a class="page-link" href="#" @click="click_page(page.number)">
                    {{ page.number }}
                </a>
            </li>
            
            <li class="page-item">
                <a class="page-link" href="#" @click="click_page(-1)">
                    >
                </a>
            </li>
        </ul>
        </nav>
    </ContentField>

</template>

<script>

import ContentField from "@/components/ContentField.vue"
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery"


export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        let users = ref([]);
        let total_users = 0;
        let current_page = 1;
        let pages = ref([]);


        const update_pages = ()=>{
            let max_pages = parseInt(Math.ceil(total_users/10));
            let new_pages = [];
            for(let i = current_page-2;i<=current_page+2;i++){
                if(i>=1 && i<=max_pages){
                    new_pages.push({
                        number :i,
                        is_active : i===current_page? "active": ""
                    });
                }
            }
            if(new_pages.length<5){
  
                for(let i=current_page+3;i<=5;i++){
                    if(i>=1 && i<=max_pages){
                        new_pages.push({
                            number :i,
                            is_active : i===current_page? "active": ""
                        });
                    }
                }

                for(let i=current_page-3;i>=1;i--){
                    if(new_pages.length<5 && i>=1 && i<=max_pages){
                        new_pages.unshift({
                            number :i,
                            is_active : i===current_page? "active": ""
                        });
                    }
                }
                
            }
            pages.value = new_pages;
        }

        const click_page = page =>{
            let max_pages = parseInt(Math.ceil(total_users/10));
            if(page===-2) page=1; // first
            if(page===-1) page=max_pages; // last
            

            if(page>=1 && page<=max_pages){
                pull_page(page);
            }
        }

        const pull_page =  page =>{
            current_page = page;
            $.ajax({
            url:"https://app5070.acapp.acwing.com.cn/api/ranklist/getlist/",
            type : "get",
            data : {
                page,
            },
            headers:{
                Authorization : "Bearer "+ store.state.user.token
            },
            success(resp){
                users.value = resp.users;
                total_users = resp.user_count;
                update_pages();
            },
            error(resp){
                console.log(resp);
            }
            })
        }

        pull_page(current_page);


        return {
            users,
            pages,
            click_page
        }
    }
}
</script>

<style scoped>
img.user-photo{
    width: 4vh;
    border-radius: 50%;
}
</style>