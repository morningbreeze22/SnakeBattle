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

        <ul class="navbar-nav">
            <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            User Name
          </a>
          <ul class="dropdown-menu">
    
            <RouterLink class="dropdown-item" :to="{name:'user_bot_index'}">My snake bots</RouterLink>
  
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#">Exit</a></li>
          </ul>
        </li>
         
            
        </ul>
        </div>
    </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';

export default{
    setup(){
        const route = useRoute();
        let route_name = computed(()=>route.name);
        return {
            route_name
        }
    }
}
</script>

<style scoped>
</style>