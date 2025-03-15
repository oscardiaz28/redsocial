import React, { useState } from 'react'
import { Box } from './components/Box'
import { Link, useNavigate } from 'react-router-dom'
import { usePageTitle } from '../../hooks/usePageTitle'
import useForm from '../../hooks/useForm'
import { useHttp } from '../../hooks/useHttp'
import { Alert } from '../../components/common/Alert'
import toast from 'react-hot-toast'

const initialObject = {
  name: "",
  surname: "",
  username: "",
  email: "",
  password: ""
}

const validate = (values) => {
  const errors = {}
  if(values.name == ""){
    errors.name = "El nombre es obligatorio"
  }
  if(values.username == ""){
    errors.username = "El username es obligatorio"
  }
  if(!values.email){
    errors.email = "El correo electrónico es obligatorio"
  }else if (!/\S+@\S+\.\S+/.test(values.email)) {
      errors.email = 'El correo electrónico no es válido';
  }
  return errors
}

export const Signup = () => {
  const navigate = useNavigate()
  usePageTitle("Registro")
  const {formData, handleChange, handleSubmit, errors, setFormData, isSubmit} = useForm(initialObject, validate, onSubmit)
  const [stateAlert, setStateAlert] = useState(false)
  const {data, loading, error, sendRequest} = useHttp({setStateAlert})

  async function onSubmit(values){
    await sendRequest({
      endpoint: "/auth/register",
      method: "POST",
      body: formData
    })
    setFormData(initialObject)
    if(!error){
      toast.success("Cuenta creada exitosamente")
      navigate("/login")
    }
  }

  return (
    <>
    <Box title={"Crea tu cuenta en Socially"}>
      <form action="" className='flex flex-col gap-5 mb-6' onSubmit={handleSubmit}>
        <div className='flex flex-col md:flex-row gap-2'>
          <div className='flex flex-col gap-2'>
            <label htmlFor="" className='font-semibold'>Nombre</label>
            <input type="text" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Tu nombre' name='name' 
            value={formData.name}
            onChange={handleChange}
            />
            {(isSubmit && errors.name) && <small className='text-red-800'>{errors.name}</small>}
          </div>
          <div className='flex flex-col gap-2'>
            <label htmlFor="" className='font-semibold'>Apellido</label>
            <input type="text" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Tu apellido' name='surname' 
            value={formData.surname}
            onChange={handleChange}
            />
            {errors.surname && <small className='text-red-800'>{errors.surname}</small>}
          </div>
        </div>
        <div className='flex flex-col gap-2'>
          <label htmlFor="" className='font-semibold'>Nombre de usuario</label>
          <input type="text" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Ejm: john123' name='username' 
          value={formData.username}
          onChange={handleChange}
          />
          {(isSubmit && errors.username) && <small className='text-red-800'>{errors.username}</small>}
        </div>
        <div className='flex flex-col gap-2'>
          <label htmlFor="" className='font-semibold'>Correo</label>
          <input type="text" className='input bg-[#131315] rounded-md border-1 w-full' placeholder='Tu correo electronico' name='email' 
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
        </div>
        
        { (error && stateAlert) && <Alert error={error} setStateAlert={setStateAlert} /> }

        <button className='btn btn-primary w-full mt-4 rounded-md bg-[#6665EC]'>
          {loading ? <span className="loading loading-spinner loading-xs"></span> : "Crear cuenta"}
        </button>
      </form>
      <p className='text-center text-sm'>¿Ya tienes una cuenta? <Link to={"/login"}
      className='text-[#6665EC] hover:underline'>Inicia Sesión</Link></p>
    </Box>
    </>
  )
}
