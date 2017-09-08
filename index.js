function loginForm () {
  let notice = document.getElementById('notice')
  notice.className = 'connecting'
  notice.innerHTML = 'Connecting...'
  
  let request = require('request')
  
  let options = {
    url: 'https://tilki-server-rootg.c9users.io/api/v1/students',
    method: 'POST',
    json: {
      'name': document.getElementById('name').value,
      'surname': document.getElementById('surname').value,
      'number': document.getElementById('number').value
    }
  }

  request(options, (error, response, body) =>{
    if (error) {
      if (navigator.onLine) {
        console.error('Sorry, something went wrong.', error)
      }
      notice.className = 'error'
      notice.innerHTML = 'There is no Internet connection.'
      return
    }
    if (response.statusCode !== 200) {
      notice.className = 'error'
      notice.innerHTML = response.statusCode + ' ' + response.statusMessage.toLowerCase() + '.'
      return
    }
    if (body.errors) {
      let errors = ''
      for (let i = 0; i < body.errors.length - 1; i++) {
        errors += body.errors[i] + '.<br>'
      }
      errors += body.errors[body.errors.length - 1]
      notice.className = 'error'
      notice.innerHTML = errors + '.'
    }
    notice.innerHTML = ''
    notice.className = ''
    return
  })
}

