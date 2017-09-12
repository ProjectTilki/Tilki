const { remote, BrowserWindow } = require('electron')

const request = require('request')

const path = require('path')
const url = require('url')

function loginForm () {
  let notice = document.getElementById('notice')
  notice.className = 'connecting'
  notice.innerHTML = 'Connecting...'
  
  
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
      } else {
        notice.className = 'error'
        notice.innerHTML = 'There is no Internet connection.'
      }
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
      return
    }
    notice.innerHTML = ''
    notice.className = ''

    let currentWindow = remote.getCurrentWindow()
    currentWindow.loadURL(url.format({
      pathname: path.join(__dirname, 'exams.html'),
      protocol: 'file:',
      slashes: true
    }))
  })
}

