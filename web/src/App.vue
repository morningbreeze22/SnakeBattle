<template>
  <div>bot:{{ bot_name }}</div>
  <div>rating:{{ bot_rating }}</div>
  <router-view/>
</template>



<script>
import $ from 'jquery';
import {ref} from 'vue';


  export default{
    name : "App",
    setup: ()=>{
      let bot_name = ref("");
      let bot_rating = ref("");
      
      $.ajax({
        url : "http://localhost:3000/pk/getbotinfo/",
        typeL: "get",
        success: resp=>{
          bot_name.value = resp.name;
          bot_rating.value =resp.rating;
        }
      });

      return {
        bot_name,
        bot_rating
      }
    }
  }
</script>

<style>
body {
  background-image: url("@/assets/FnYBabLaEAQqcfR.png");
  background-size: cover;
}
</style>
