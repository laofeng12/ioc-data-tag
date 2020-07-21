// import 'css.escape'

const defaultOpts = {
  // required opts
  Vue: null,
  appOptions: null,
  template: null
}

export default function singleSpaVue (userOpts) {
  if (typeof userOpts !== 'object') {
    throw new Error(`single-spa-vue requires a configuration object`)
  }

  const opts = {
    ...defaultOpts,
    ...userOpts
  }

  if (!opts.Vue) {
    throw Error('single-spa-vue must be passed opts.Vue')
  }

  if (!opts.appOptions) {
    throw Error('single-spa-vue must be passed opts.appOptions')
  }

  if (
    opts.appOptions.el &&
    typeof opts.appOptions.el !== 'string' &&
    !(opts.appOptions.el instanceof HTMLElement)
  ) {
    throw Error(
      `single-spa-vue: appOptions.el must be a string CSS selector, an HTMLElement, or not provided at all. Was given ${typeof opts
        .appOptions.el}`
    )
  }

  // Just a shared object to store the mounted object state
  let mountedInstances = {}

  return {
    bootstrap: bootstrap.bind(null, opts, mountedInstances),
    mount: mount.bind(null, opts, mountedInstances),
    unmount: unmount.bind(null, opts, mountedInstances),
    update: update.bind(null, opts, mountedInstances)
  }
}

function bootstrap (opts) {
  if (opts.loadRootComponent) {
    return opts.loadRootComponent().then(root => (opts.rootComponent = root))
  } else {
    return Promise.resolve()
  }
}

function mount (opts, mountedInstances, props) {
  return Promise.resolve().then(() => {
    if (window.singleSpaConfig.spaProjects[props.name].instance) {
      const app = document.getElementById('app')
      app.appendChild(window.singleSpaConfig.spaProjects[props.name].instance.$el)
      return window.singleSpaConfig.spaProjects[props.name].instance
    }

    const appOptions = { ...opts.appOptions }
    if (props.domElement && !appOptions.el) {
      appOptions.el = props.domElement
    }

    let domEl
    if (appOptions.el) {
      if (typeof appOptions.el === 'string') {
        domEl = document.querySelector(appOptions.el)
        if (!domEl) {
          throw Error(
            `If appOptions.el is provided to single-spa-vue, the dom element must exist in the dom. Was provided as ${appOptions.el}`
          )
        }
      } else {
        domEl = appOptions.el
      }
    } else {
      const htmlId = `single-spa-application:${props.name}`
      // eslint-disable-next-line no-useless-escape
      appOptions.el = `#single-spa-application\:${props.name}`
      domEl = document.getElementById(htmlId)
      if (!domEl) {
        domEl = document.createElement('div')
        domEl.id = htmlId
        document.body.appendChild(domEl)
      }
    }

    appOptions.el = appOptions.el + ' .single-spa-container'

    // single-spa-vue@>=2 always REPLACES the `el` instead of appending to it.
    // We want domEl to stick around and not be replaced. So we tell Vue to mount
    // into a container div inside of the main domEl
    if (!domEl.querySelector('.single-spa-container')) {
      const singleSpaContainer = document.createElement('div')
      singleSpaContainer.className = 'single-spa-container'
      domEl.appendChild(singleSpaContainer)
    }

    mountedInstances.domEl = domEl

    if (!appOptions.render && !appOptions.template && opts.rootComponent) {
      appOptions.render = h => h(opts.rootComponent)
    }

    if (!appOptions.data) {
      appOptions.data = {}
    }

    appOptions.data = { ...appOptions.data, ...props }

    mountedInstances.instance = new opts.Vue(appOptions)
    if (mountedInstances.instance.bind) {
      mountedInstances.instance = mountedInstances.instance.bind(
        mountedInstances.instance
      )
    }
    window.singleSpaConfig.spaProjects[props.name].instance = mountedInstances.instance
    return mountedInstances.instance
  })
}

function update (opts, mountedInstances, props) {
  return Promise.resolve().then(() => {
    const data = {
      ...(opts.appOptions.data || {}),
      ...props
    }
    for (let prop in data) {
      mountedInstances.instance[prop] = data[prop]
    }
  })
}

function unmount (opts, mountedInstances) {
  return Promise.resolve().then(() => {
    const app = document.getElementById('app')
    app.innerHTML = ''
    // 隐藏起来~
    // mountedInstances.instance.$el.style.display = 'none'

    // mountedInstances.instance.$destroy()
    // mountedInstances.instance.$el.innerHTML = ''
    // delete mountedInstances.instance

    // if (mountedInstances.domEl) {
    //   mountedInstances.domEl.innerHTML = ''
    //   delete mountedInstances.domEl
    // }
  })
}