import React, { useState } from 'react'
import { Box } from './components/Box'
import { Link, useNavigate } from 'react-router-dom'
import { usePageTitle } from '../../hooks/usePageTitle'
import useForm from '../../hooks/useForm'
import { request } from '../../libs/api'
import { Alert } from '../../components/common/Alert'
import useAuth from '../../hooks/useAuth'
import toast from 'react-hot-toast'

const initialObject = {
  email: "",
  password: ""
}

const validate = (values) => {
  const errors = {}
  if(values.email == ""){
    errors.email = "El correo electrónico es obligatorio"
  }
  if(values.password == ""){
    errors.password = "El password es obligatorio"
  }
  if(!values.email){
    errors.email = "El correo electrónico es obligatorio"
  }else if (!/\S+@\S+\.\S+/.test(values.email)) {
      errors.email = 'El correo electrónico no es válido';
  }
  return errors
}

export const Login = () => {

  usePageTitle("Login")
  const {checkAuth} = useAuth()
  const navigate = useNavigate();
  const [backError, setBackError] = useState({})
  const [stateAlert, setStateAlert] = useState(false)
  const {formData, handleChange, handleSubmit, errors, isSubmit} = useForm(initialObject, validate, onSubmit)
  const [loading, setLoading] = useState(false)


  async function onSubmit(values){
    setLoading(true)
    await request({
      endpoint: "/auth/login",
      method: "POST",
      body: values,
      onSuccess: async () => {
        await checkAuth()
        toast.success("¡Inicio de sesión exitoso!")
        navigate("/")
      },
      onFailure: (err) => {
        setStateAlert(true)
        setBackError(err)
      }
    })
    setLoading(false)
  }

  return (
    <Box title={"Bienvenido a Socially"}>
      <form action="" className='flex flex-col gap-5 mb-6' onSubmit={ handleSubmit }>
        <div className='flex flex-col gap-2'>
          <label htmlFor="" className='font-semibold'>Correo</label>
          <input type="text" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Tu correo electronico' 
          name='email' 
          value={formData.email}
          onChange={handleChange}
          />
          {(isSubmit && errors.email) && <small className='text-red-800'>{errors.email}</small>}
        </div>
        <div className='flex flex-col gap-2'>
          <label htmlFor="" className='font-semibold'>Contraseña</label>
          <input type="password" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Tu contraseña' name='password' 
          value={formData.password}
          onChange={handleChange}
          />
          {(isSubmit && errors.password) && <small className='text-red-800'>{errors.password}</small>}
        </div>  
        { (backError && stateAlert) && <Alert error={backError} setStateAlert={setStateAlert} /> }
        <button className='btn btn-primary w-full mt-4 rounded-md bg-[#6665EC]'>
          {loading ? <span className="loading loading-spinner loading-xs"></span> : "Iniciar Sesión"}
        </button>
      </form>
      <p className='text-center text-sm'>¿Aun no tienes cuenta? <Link to={"/signup"}
      className='text-[#6665EC] hover:underline'>Comienza aqui</Link></p>
    </Box>
  )
}
  