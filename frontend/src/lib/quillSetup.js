import { QuillEditor } from '@vueup/vue-quill'

import '@vueup/vue-quill/dist/vue-quill.snow.css'
import api from '@/lib/api'

const globalOptions = {
  modules: {
    toolbar: {
      container: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote'],
        [{ size: ['small', false, 'large', 'huge'] }],
        [{ color: [] }, { background: [] }],
        ['image'],
      ],
      handlers: {
        image: imageHandler,
      },
    },
  },
  theme: 'snow',
}

function imageHandler() {
  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')
  input.click()

  input.onchange = async () => {
    const file = input.files[0]
    if (!file) return

    // 서버에 이미지 업로드
    const formData = new FormData()
    formData.append('image', file)

    try {
      const response = await api.post('/posts/image', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })

      const imageUrl = response.data.result

      // Quill 인스턴스 가져오기
      const quill = this.quill || this // this가 다르면 조정 필요
      const range = quill.getSelection(true)

      // 이미지 URL 삽입
      quill.insertEmbed(range.index, 'image', imageUrl)
      quill.setSelection(range.index + 1)
    } catch (error) {
      console.error('이미지 업로드 실패', error)
    }
  }
}

export function registerQuill(app) {
  QuillEditor.props.globalOptions.default = () => globalOptions
  app.component('QuillEditor', QuillEditor)
}
