import React from 'react'
import { Header } from '../components/common/Header'
import { Navigate, Outlet, useNavigate } from 'react-router-dom'
import { Sidebar } from '../components/common/Sidebar'
import { RightPanel } from '../components/common/RightPanel'
import useAuth from '../hooks/useAuth'

export const PrivateLayout = () => {

  const {isAuthenticated, user, loading} = useAuth()

  if (loading.fetch) return <p>Loading...</p>;

  return (
    <>
      
      {isAuthenticated ? (
        <div >
          <Header />
          <div className='max-w-screen-xl mx-auto'>
            <div className='w-full mt-[85px] px-4 lg:grid lg:grid-cols-[18rem_1fr_22rem] lg:gap-6 items-start'>
              <Sidebar  />
              <div className='border-1 border-gray-800 rounded-md p-4 mb-4'>
                <Outlet />
              </div>
              <RightPanel />
            </div>
          </div>
        </div>
      )
      : <Navigate to="/login" />
      }
  
    </>
  )
}
