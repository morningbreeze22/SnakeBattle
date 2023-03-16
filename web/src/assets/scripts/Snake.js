import { GameObject } from "./GameObject";
import { Cell } from "./Cell";

export class Snake extends GameObject{
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        // cells[0] is head of snake
        this.cells = [new Cell(info.r,info.c)];
        this.next_cell = null;

        this.speed = 5; //5 blocks per sec

        this.direction = -1;  //next direction, 0123-up,right,down,left

        this.status = "idle"; //status: idle, move, die


        this.dr = [-1,0,1,0]; //delta in row
        this.dc = [0,1,0,-1]; //delta in col

        this.step=0;

        this.eps = 1e-2;

        // used to render eyes
        this.eye_direction =0;
        if(this.id==1) this.eye_direction=2;

        this.eye_dx = [
            [-1,1],
            [1,1],
            [1,-1],
            [-1,-1]
        ];
        this.eye_dy = [
            [-1,-1],
            [-1,1],
            [1,1],
            [-1,1]
        ];

    }

    
    start(){

    }

    // set direction
    // to do: add backend
    set_direction(d){
        this.direction = d;
    }

    // rules for when a snake will become longer
    check_tail_increase(){
        if(this.step<=10) return true;
        if(this.step%3 === 1) return true;
        return false;
    }

    // update snake with info of next step, called in gamemap.js
    next_step(){
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r+this.dr[d],this.cells[0].c+this.dc[d]);
        
        this.eye_direction = d;
        this.direction = -1;
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        for(let i=k;i>0;i--){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1]));
        }

        // used to debug at frontend
        /* 
        if(!this.gamemap.check_valid(this.next_cell)){
            this.status = "die";
        }
        */
    }

    update_move(){
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx*dx+dy*dy);
        if(distance<this.eps){
            this.cells[0] = this.next_cell; 
            this.next_cell = null;
            this.status = "idle";

            if(!this.check_tail_increase()){
                this.cells.pop();
            }
        }else{
            // move head
            const move_distance = this.speed*this.timedelta/1000;
            this.cells[0].x += move_distance*dx/distance;
            this.cells[0].y += move_distance*dy/distance;

            // move tail
            if(!this.check_tail_increase()){
                const k = this.cells.length;
                const tail = this.cells[k-1];
                const tail_target = this.cells[k-2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                // here assume that head and tail have same speed and disatance
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }

    }

    update(){
        if(this.status==='move'){
            this.update_move();
        }
        
        this.render();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;

        if(this.status==="die"){
            ctx.fillStyle = "White";
        }

        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x*L,cell.y*L,L/2*0.8,0,Math.PI*2);
            ctx.fill();
        }

        for(let i=1;i<this.cells.length;i++){
            const a = this.cells[i-1];
            const b = this.cells[i];

            if(Math.abs(a.x-b.x)<this.eps && Math.abs(a.y-b.y)<this.eps){
                continue;
            }
            if(Math.abs(a.x-b.x)<this.eps){
                ctx.fillRect((a.x-0.4)*L, Math.min(a.y,b.y)*L,L*0.8,Math.abs(a.y-b.y)*L);
            }else{
                ctx.fillRect(Math.min(a.x,b.x)*L,(a.y-0.4)*L,Math.abs(a.x-b.x)*L, L*0.8);
            }

        }

        ctx.fillStyle = "black";
        for(let i=0;i<2;i++){
            const eye_x = (this.cells[0].x +this.eye_dx[this.eye_direction][i]*0.15)*L;
            const eye_y = (this.cells[0].y +this.eye_dy[this.eye_direction][i]*0.15)*L;
            ctx.beginPath();
            ctx.arc(eye_x,eye_y,L*0.1,0,Math.PI*2);
            ctx.fill();
        }
    }
}