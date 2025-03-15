import React from 'react'
import { Navigate, Outlet, useNavigate } from 'react-router-dom'
import useAuth from '../hooks/useAuth'

export const AuthLayout = () => {

  const {isAuthenticated, loading} = useAuth();

  if (loading.fetch) return <p>Loading...</p>;

  return (
    isAuthenticated ? <Navigate to="/" /> : <Outlet />
  )
  
}
