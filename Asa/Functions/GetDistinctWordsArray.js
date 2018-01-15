// gets distinct words that aren't hashtags or usermentions or stopWords
function main(text) {
    // update this list for additional stop words
    var stopWords = {
        'the': 0,
        'a': 0,
        'and': 0
    }

    var punctuations = {
        ',': 0,
        '.': 0,
        '@': 0,
        '#': 0,
        ';': 0,
        '&': 0,
        '!': 0,
        ':': 0,
        '-': 0,
        '(': 0,
        ')': 0,
        '[': 0,
        ']': 0,
        ' ': 0
    }


    var words = text.toLowerCase().split(" ")
    var distinctWords = {}
    var trimPunctuations = function (s) {
        if (s.length == 0) {
            return s
        }

        var startIndex = 0
        while (startIndex < s.length && s.charAt(startIndex) in punctuations) {
            startIndex++
        }

        var endIndex = s.length - 1
        while (endIndex >= 0 && s.charAt(endIndex) in punctuations) {
            endIndex--
        }

        return (endIndex + 1 - startIndex) < 0 ? '' : s.substring(startIndex, endIndex + 1)
    }

    var isOnlyNumbers = function (s) {
        var result = true
        for (var i = 0; i < s.length && result; i++) {
            result = result && (s.charAt(i) in punctuations || (s.charAt(i) >= '0' && s.charAt(i) <= '9'))
        }

        return result
    }

    for (var i = 0; i < words.length; i++) {
        var word = words[i]
        if (word.length != 0
            && word.charAt(0) != '@'
            && word.charAt(0) != '#'
            && !(word in stopWords)
            && word.substring(0, 4) != 'http') {
            word = trimPunctuations(word, punctuations).toLowerCase()
            if (word.length > 0 && !isOnlyNumbers(word)) {
                distinctWords[word] = true
            }
        }
    }

    return Object.keys(distinctWords);
}