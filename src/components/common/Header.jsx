import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { IoMenu } from "react-icons/io5";
import { GoHome } from "react-icons/go";
import { IoIosCloseCircleOutline } from "react-icons/io";
import { FaRegBell } from "react-icons/fa";
import { FaRegUser } from "react-icons/fa6";
import { IoExitOutline } from "react-icons/io5";
import useAuth from '../../hooks/useAuth';


export const Header = () => {

  const [openMenu, setOpenMenu] = useState(false)
  const {logout} = useAuth()

  useEffect(() => {
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 1024) {
        setOpenMenu(false)
      }
    })
  }, [])

  return (
    <div className='flex items-center justify-between border-b-1 border-gray-800 h-[60px] bg-[#090909]/60 backdrop-blur-lg shadow-md lg:mb-0 fixed w-full right-0 top-0'>

      <div className='lg:max-w-screen-xl h-full w-full mx-auto flex items-center px-4 justify-between'>

        <Link className='font-bold text-2xl' to={"/"}>Socially</Link>

        <button className='cursor-pointer lg:hidden' onClick={() => setOpenMenu(true)}>
          <IoMenu className='text-2xl' />
        </button>

        <nav className={`mobile w-1/2 md:w-1/3 p-4 px-5 top-0 bg-[#090909] border-s-1 border-gray-700 right-0 h-screen  ${openMenu ? 'fixed' : 'hidden'}`}>
          <button className='absolute top-4 right-4 cursor-pointer' onClick={() => setOpenMenu(false)}>
            <IoIosCloseCircleOutline className='text-2xl' />
          </button>
          <p className='text-center font-bold mt-4 text-lg mb-6'>Menu</p>

          <ul className='flex flex-col gap-3 h-[90%]'>
            <Link
              className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
              <GoHome />
              <p>Home</p>
            </Link>
            <Link
              className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
              <FaRegBell />
              <p>Notifications</p>
            </Link>
            <Link
              className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
              <FaRegUser />
              <p>Profile</p>
            </Link>

            <Link
              className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white mt-auto mb-4'>
              <IoExitOutline />
              <p>Cerrar Sesión</p>
            </Link>
          </ul>
        </nav>

        <nav className='desktop hidden lg:flex gap-4'>
          <Link to={"/"}
            className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
            <GoHome />
            <p>Feed</p>
          </Link>
          <Link to={"/personas"}
            className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
            <FaRegBell />
            <p>Personas</p>
          </Link>
          <Link to={"/profile"}
            className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white'>
            <FaRegUser />
            <p>Perfil</p>
          </Link>
          <button
          onClick={ logout }
            className='flex gap-3 items-center hover:bg-[#252525] p-2 w-full px-4 rounded-md hover:text-white cursor-pointer'>
            <IoExitOutline />
          </button>
        </nav>

      </div>


    </div>
  )
}
