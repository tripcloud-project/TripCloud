import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import DriveView from '@/views/DriveView.vue'
import BoardListView from '@/views/BoardListView.vue'
import DriveTestView from '@/views/DriveTestView.vue'
import PostDetailView from '@/views/PostDetailView.vue'
import NewPostView from '@/views/NewPostView.vue'
import MapView from '@/views/MapView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/drive',
      name: 'drive',
      component: DriveView,
    },
    {
      path: '/board',
      name: 'board',
      component: BoardListView,
    },
    {
      path: '/drive-test',
      name: 'drive-test',
      component: DriveTestView,
    },
    {
      path: '/posts/:id',
      name: 'posts',
      component: PostDetailView,
      props: true,
    },
    {
      path: '/posts/new',
      name: 'newPost',
      component: NewPostView,
    },
    {
      path: '/drive/map',
      name: 'map',
      component: MapView,
    },
  ],
})

export default router
