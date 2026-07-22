<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    msg: {
      type: Boolean,
      default: false
    }
  },
  render (h, context) {
    const { icon, title, msg } = context.props
    const vnodes = []
    if (icon) {
      icon.indexOf('yu-icon') > -1 ? vnodes.push(<i class={icon}></i>) : vnodes.push(<svg-icon icon-class={icon} />)
    }

    if (title) {
      vnodes.push(<span slot="title">{(title)}</span>);
      msg || vnodes.push(<i class="msg-prompt"></i>);
    }
    return vnodes
  }
}
</script>
