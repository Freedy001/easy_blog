export function copyProperties(source:any,dest:any) {
    Object.keys(source).forEach(key=>{
        dest[key]=source[key];
    })
}


