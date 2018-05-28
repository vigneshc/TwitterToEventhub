// stringify javascript object.

function main(arrayOfObjects) {
    if(arrayOfObjects == undefined)
    {
        return "[]"
    }
    
    return JSON.stringify(arrayOfObjects)
}