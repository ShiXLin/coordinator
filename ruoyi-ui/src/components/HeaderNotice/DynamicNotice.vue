<template>
  <component
    :is="comp"
    :formData="formData"
    ref="compModel"
    v-if="comp">
  </component>
</template>
<script>
  export default {
    name: 'DynamicNotice',
    data () {
      return {
        compName: this.path
      }
    },
    computed: {
      comp: function () {
        if(!this.path){
          return null;
        }
        //return () => import(`@/views/${this.path}.vue`)
        //去掉这个编译警告Critical dependency: the request of a dependency is an expression
        return () => Promise.resolve(require(`@/views/${this.path}.vue`).default)
      }
    },
    props: ['path','formData'],
    methods: {
      detail () {
        setTimeout(() => {
          if(this.path){
            this.$refs.compModel.view(this.formData);
          }
        }, 200)
      },
    }
  }
</script>