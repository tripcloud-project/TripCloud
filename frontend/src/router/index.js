import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import DriveView from '@/views/DriveView.vue'
import BoardListView from '@/views/BoardListView.vue'
import PostDetailView from '@/views/PostDetailView.vue'
import NewPostView from '@/views/NewPostView.vue'
import DriveTrashView from '@/views/DriveTrashView.vue'
import MapView from '@/views/MapView.vue'
import ProfileView from '@/views/ProfileView.vue'
import ProfileEditView from '@/views/ProfileEditView.vue'

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
      path: '/drive/trash',
      name: 'trash',
      component: DriveTrashView,
    },
    {
      path: '/drive/map',
      name: 'map',
      component: MapView,
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
    },
    {
      path: '/profile/edit',
      name: 'profileEdit',
      component: ProfileEditView,
    },
  ],
})

export default router
