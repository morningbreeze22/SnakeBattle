<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <RouterLink class="navbar-brand" :to="{name : 'home'}">Snake Battle</RouterLink>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
            <RouterLink :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" :to="{name:'pk_index'}">Battle</RouterLink>
            </li>
            <li class="nav-item">
            <RouterLink :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name:'record_index'}">Record</RouterLink>
            </li>
            <li class="nav-item">
            <RouterLink :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'"  :to="{name:'ranklist_index'}">Ranking</RouterLink>
            </li>
            
        </ul>

        <ul class="navbar-nav" v-if="$store.state.user.is_login">
            <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{$store.state.user.username}}
          </a>
          <ul class="dropdown-menu">
    
            <RouterLink class="dropdown-item" :to="{name:'user_bot_index'}">My snake bots</RouterLink>
  
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">Exit</a></li>
          </ul>
            </li>
        </ul>

        <ul class="navbar-nav" v-else>
            <li class="nav-item dropdown">
          <RouterLink class="nav-link" :to="{name:'user_account_login'}" href="#" role="button" aria-expanded="false">
            Login
          </RouterLink>
            </li>

            <li class="nav-item dropdown">
          <RouterLink  class="nav-link" :to="{name:'user_account_register'}" href="#" role="button"  aria-expanded="false">
            Register
          </RouterLink>
            </li>
        </ul>



        </div>
    </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default{
    setup(){
        const route = useRoute();
        const store = useStore();
        let route_name = computed(()=>route.name);

        const logout = ()=>{
            store.dispatch("logout");
        }

        return {
            route_name,
            logout
        }
    }
}
</script>

<style scoped>
</style>