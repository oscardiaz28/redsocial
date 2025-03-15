import React from 'react'
import { Header } from './Header'
import { useLocation, useNavigate } from 'react-router-dom'
import { usePageTitle } from '../../hooks/usePageTitle'

export const Error404 = () => {
    usePageTitle("Error 404")
    const location = useLocation()
    const navigate = useNavigate();

    const destination = location.state?.from || "/"

  return (
    <div>
        <Header />
        <div className='min-h-screen flex items-center justify-center flex-col gap-8'>
            <p className='text-2xl'><span className='font-bold'>404</span> <span>|</span> Esta pagina no se pudo encontrar </p>
            <button className='btn btn-sm btn-secondary rounded-md' onClick={() => navigate(destination) }>Regresar</button>
        </div>
    </div>
  )
}
