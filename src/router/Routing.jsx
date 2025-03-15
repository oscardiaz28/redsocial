import React from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthLayout } from '../layouts/AuthLayout'
import { Login } from '../pages/auth/Login'
import { Signup } from '../pages/auth/Signup'
import { PrivateLayout } from '../layouts/PrivateLayout'
import { HomePage } from '../pages/home/HomePage'
import { PeoplePage } from '../pages/people/PeoplePage'
import { NotificationsPage } from '../pages/notifications/NotificationsPage'
import { ProfilePage } from '../pages/profile/ProfilePage'
import { Error404 } from '../components/common/Error404'
import { AuthProvider } from '../context/AuthProvider'
import { FollowPage } from '../pages/people/follow/FollowPage'
import { FollowerPage } from '../pages/people/follower/FollowerPage'
import { PostProvider } from '../context/PostProvider'
import { PublicationsPage } from '../pages/people/publications/PublicationsPage'
import { ProfileUser } from '../pages/profile/ProfileUser'

export const Routing = () => {
  return (
    <BrowserRouter>

      <AuthProvider>
        <PostProvider>
          
          <Routes>
            <Route element={<AuthLayout />}>
              <Route path='login' element={<Login />} />
              <Route path="signup" element={<Signup />} />
            </Route>

            
            <Route path='/' element={<PrivateLayout />}>
              <Route index element={<HomePage />} />
              <Route path='personas' element={<PeoplePage />} />
              <Route path='notifications' element={<NotificationsPage />} />
              <Route path='profile' element={<ProfilePage />} />
              <Route path='profile/:username' element={<ProfileUser />} />
              <Route path='siguiendo' element={<FollowPage />} />
              <Route path='seguidores' element={<FollowerPage />} />
              <Route path='publicaciones' element={<PublicationsPage />} />
            </Route>

            <Route path='*' element={<Error404 />} />
          </Routes>

        </PostProvider>
      
      </AuthProvider>

    </BrowserRouter>
  )
}
