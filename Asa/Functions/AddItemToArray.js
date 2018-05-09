// Adds an element to given array
function main(arrayArg, item) {
    if(arrayArg == undefined)
    {
        return arrayArg
    }
    
    arrayArg.push(JSON.parse(item))
    return arrayArg;
}