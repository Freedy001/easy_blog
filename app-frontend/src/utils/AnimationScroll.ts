/**
 * 滚动动画过度
 * @param {Object} position 定位（只支持Y轴）
 * @param {Number} delay 单位毫秒 default 200
 * @param {Number} speed 单位毫秒 default 10
 * 误差：滚动距离越短误差越小
 */
export class AnimationScrollTop {
    private position: any;
    private readonly delay: number;
    private readonly speed: number;
    private readonly dimension: number;
    private readonly step: number;
    private readonly thisTop: number;
    private readonly upOrDown: boolean;
    private delayt: number;
    private delays: any;
    constructor (position: any, delay = 200, speed = 10) {
        this.position = position
        this.delay = delay
        this.speed = speed
        this.step = this.delay / this.speed
        this.dimension = this.position.y / this.step
        this.thisTop = window.pageYOffset
        this.delayt = this.thisTop
        this.upOrDown = this.thisTop > this.position.y
        this.delays = null
        // 初始化
        this.init()
    }
    init () {
        this.delays = setInterval(() => {
            if (!this.upOrDown) {
                if (this.delayt >= this.position.y) {
                    clearInterval(this.delays)
                }
                this.delayt += this.dimension
            } else {
                if (this.delayt <= this.position.y) {
                    clearInterval(this.delays)
                }
                this.delayt -= this.dimension
            }
            window.scrollTo(this.position.x, this.delayt)
        }, this.speed)
    }
}