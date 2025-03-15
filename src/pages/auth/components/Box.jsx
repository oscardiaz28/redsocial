import React from 'react'
import { Link } from 'react-router-dom'

export const Box = ( {children, title} ) => {
  return (
    <div className='flex items-center justify-center flex-col min-h-screen px-4 py-10 lg:py-0'>
        <h2 className='mb-12 text-3xl font-bold'>{title}</h2>
        <div className='p-10 bg-[#19191C] shadow-lg rounded-xl border-1 border-gray-700 w-full max-w-[450px]'>
            {children}
        </div>
    </div>
  )
}
