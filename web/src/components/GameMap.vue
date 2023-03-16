<template>
  <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
        
  </div>
  <div class="col-12 row-3" style="text-align: center;">
        <div class="card game_message">
            <p v-if="$store.state.pk.a_id==$store.state.user.id ">You are BLUE snake start at left bottom corner!</p>
            <p v-if="$store.state.pk.b_id==$store.state.user.id">You are RED snake start at right upper corner!</p>
        </div>
    </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex';

export default{
    setup(){
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(()=>{
            store.commit("updateGameObject",
                new GameMap(canvas.value.getContext("2d"),parent.value, store)
            );
        })
        return {
            parent,
            canvas
        }
    }
}
</script>

<style>
div.gamemap{
    width:100%;
    height:100%;
    display:flex;
    justify-content: center;
    align-items: center;
}
div.game_message{
    background-color: rgba(255, 255, 255, 0.8);
}
</style>