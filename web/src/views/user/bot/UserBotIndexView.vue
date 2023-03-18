<template>
    <div class="container">
        <div class="row" style="margin-top: 20px">
            <div class="col-3">
                <div class="card" >
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width:100%">
                    </div>
                </div>
            </div>

            <div class="col-9">
                <div class="card">
                    <div class="card-header">
                        <span style="font-size:130%">My snake bots</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#btn-add-bot">
                            Create new snake
                        </button>


                        <!-- Modal -->
                        <div class="modal fade" id="btn-add-bot" tabindex="-1">
                        <div class="modal-dialog modal-xl">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">Create snake bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form @submit.prevent="register">
                                    <div class="mb-3">
                                    <label for="title" class="form-label">Name:</label>
                                    <input v-model="botadd.title" type="text" id="title" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                    <label for="description" class="form-label">Description:</label>
                                    <textarea  v-model="botadd.description" id="description" rows="6" class="form-control" />
                                    </div>

                                    <div class="mb-3">
                                    <label for="code" class="form-label">Code:</label>
                                    <VAceEditor
                                        v-model:value="botadd.content"
                                        lang="java"
                                        theme="textmate"
                                        style="height: 300px"
                                        :options="{
                                        enableBasicAutocompletion: true, 
                                        enableSnippets: true, 
                                        enableLiveAutocompletion: true,
                                        fontSize: 14, 
                                        tabSize: 2, 
                                        showPrintMargin: false, 
                                        highlightActiveLine: true,
                                        }"
                                    >abcd</VAceEditor>

                                    </div>
                                    
                                    

                            
                                </form>
                            </div>
                            <div class="modal-footer">
                                <div class="error-message">{{botadd.error_message}}</div>
                                <button type="button" class="btn btn-primary" @click="add_bot">Create</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Create time</th>
                                    <th>Operations</th>
                                </tr>
                            </thead>

                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createTime }} (PST)</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" data-bs-toggle="modal" :data-bs-target="'#btn-add-bot'+bot.id">Modify</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">Delete</button>
                                    </td>


                                        <!-- Modal -->
                                        <div class="modal fade" :id =" 'btn-add-bot' + bot.id" tabindex="-1">
                                        <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Modify snake bot</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <form @submit.prevent="register">
                                                    <div class="mb-3">
                                                    <label for="title" class="form-label">Name:</label>
                                                    <input v-model="bot.title" type="text" id="title" class="form-control">
                                                    </div>

                                                    <div class="mb-3">
                                                    <label for="description" class="form-label">Description:</label>
                                                    <textarea  v-model="bot.description" id="description" rows="3" class="form-control" />
                                                    </div>

                                                    <div class="mb-3">
                                                    <label for="code" class="form-label">Code:</label>
                                                    <VAceEditor
                                                        v-model:value="bot.content"
                                                        lang="java"
                                                        theme="textmate"
                                                        style="height: 300px"
                                                        :options="{
                                                        enableBasicAutocompletion: true, 
                                                        enableSnippets: true, 
                                                        enableLiveAutocompletion: true,
                                                        fontSize: 14, 
                                                        tabSize: 2, 
                                                        showPrintMargin: false, 
                                                        highlightActiveLine: true,
                                                        }"
                                                    />
                                                    </div>
                                                    
                                                    

                                            
                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <div class="error-message">{{botadd.error_message}}</div>
                                                <button type="button" class="btn btn-primary" @click="update_bot(bot)">Submit</button>
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                            </div>
                                            </div>
                                        </div>
                                        </div>

                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
import { ref, reactive } from "vue"
import $ from "jquery"
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/mode-java.js';
//import 'ace-builds/src-noconflict/mode-c_cpp.js';




export default{
    components:{
        VAceEditor
    },
    setup(){
        const store = useStore();
        

        let bots = ref("");

        const botadd = reactive({
            title:"",
            description:`Welcome to snake battle!You can create a new snake bot by modifying code below. Here are some details that you need to know.
Now we only support Java code. You only need to change code in nextMove() function. DO NOT change class name and package name.
Input of this function is a string contains all the information about this game, assume you are player a and your opponent is b, format of this string is:

gamemap#a.sx#a.sy#(a.steps)#b.sx#b.sy#(b.steps)

gamemap is a 13*14 0-1 string, 1 means wall.
sx and sy are x and y index of start points.
steps surrounded by round brackets are previous steps of each player, they are string consist of number 0,1,2,3, which means up,right,down,left. 

Output of this function is also an integer that decides which direction your snake will go to , it should belong to {0,1,2,3}, which means up,right,down,left respectively.

We already provide some code to parse the input, so the easiest way to modify it is to change code between line 61-70.

Good luck!`,
            content:`package com.snakebattle.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.snakebattle.botrunningsystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {  // define a cell with index
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step) { // rules for when the snake will become longer
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells(int sx, int sy, String steps) { // compute snake cells
        steps = steps.substring(1, steps.length() - 1); // get rid of brackets
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i ++ ) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
                res.remove(0);
            }
        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {   
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0, k = 0; i < 13; i ++ ) {
            for (int j = 0; j < 14; j ++, k ++ ) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;    // g is gamemap, 1 means wall
                }
            }
        }
        // get start points
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);
        // compute cells
        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);
        
        for (Cell c: aCells) g[c.x][c.y] = 1;
        for (Cell c: bCells) g[c.x][c.y] = 1;
        // replace below code with your own algorithm!
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i ++ ) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }

        return 0;
    }
}
`,
            error_message:""
        });

        ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" +
            require("ace-builds").version +
            "/src-noconflict/");
   


        // refresh page and fetch all bots from backend
        const refresh_bots = ()=>{
            $.ajax({
            url:"http://localhost:3000/user/bot/getlist/",
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

        // create new snake
        const add_bot = ()=>{
            botadd.error_message= "";
            $.ajax({
                url:"http://localhost:3000/user/bot/add/",
                type:"post",
                contentType: "application/json",
                data:JSON.stringify({
                    title:botadd.title,
                    description:botadd.description,
                    content: botadd.content,
                }),
                headers:{
                    Authorization : "Bearer "+ store.state.user.token
                },
                success(resp){
                    if(resp.error_message==="success"){
                        botadd.title="";
                        botadd.description="";
                        botadd.content="";
                        Modal.getInstance("#btn-add-bot").hide();
                        refresh_bots();
                    } else{
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        // update bot with new info
        const update_bot = (bot)=>{
            botadd.error_message= "";
            $.ajax({
                url:"http://localhost:3000/user/bot/update/",
                type:"post",
                contentType: "application/json",
                data:JSON.stringify({
                    bot_id : bot.id,
                    title:bot.title,
                    description:bot.description,
                    content: bot.content,
                }),
                headers:{
                    Authorization : "Bearer "+ store.state.user.token
                },
                success(resp){
                    if(resp.error_message==="success"){
                        Modal.getInstance("#btn-add-bot"+bot.id).hide();
                        refresh_bots();
                    } else{
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }


   

        // remove some snake
        const remove_bot = (bot)=>{
            if(confirm("Do you really want to delete this snake bot?")){
                $.ajax({
                url:"http://localhost:3000/user/bot/remove/",
                type:"post",
                contentType: "application/json",
                data:JSON.stringify({
                    bot_id : bot.id,
                }),
                headers:{
                    Authorization : "Bearer "+ store.state.user.token
                },
                success(resp){
                    if(resp.error_message==="success"){
                        refresh_bots();
                    } else{
                        botadd.error_message = resp.error_message;
                    }
                }
            })
            }
        
        }

        return {
            bots,
            botadd,
            add_bot,
            update_bot,
            remove_bot,
        }
        
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
}
</style>