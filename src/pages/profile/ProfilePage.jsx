import React, { useEffect, useState } from 'react'
import useAuth from '../../hooks/useAuth'
import toast from 'react-hot-toast'

export const ProfilePage = () => {

  const {user, loading, updateProfile} = useAuth()
  const [formData, setFormData] = useState({
    name: "",
    surname: "",
    username: "",
    bio: "",
    country: "",
    link: "",
    position: ""
  })
  const [error, setError] = useState(null)

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try{
      await updateProfile(formData)
      toast.success("Datos actualizados correctamente")
      setError(null)
    }catch(err){
      const message = err.response?.data.message
      setError(message)
    }
  }
  
  useEffect(() => {
    if (user) {
      setFormData({
        name: user.name || "",
        surname: user.surname || "",
        username: user.username || "",
        bio: user.bio || "",
        country: user.country || "",
        link: user.link || "",
        position: user.position || ""
      })
    }
  }, [user])


  return (
    <div>
      <h3 className='font-bold textmd mb-4'>Datos Personales</h3>

      { loading.mutation && (
        <div className='flex flex-col gap-3'>
          <div className='grid grid-cols-[1fr_2fr] gap-2'>
            <div className="skeleton h-7 w-full rounded-md"></div>
            <div className="skeleton h-7 w-full rounded-md"></div>
          </div>
          <div className='grid grid-cols-[1fr_2fr] gap-2'>
            <div className="skeleton h-7 w-full rounded-md"></div>
            <div className="skeleton h-7 w-full rounded-md"></div>
          </div>
        </div>
      )}

      { !loading.mutation && (
        <form action="" className='card flex flex-col gap-5' onSubmit={handleSubmit}>
          { ["name", "surname", "username", "bio", "link", "country", "position"].map( (field, index) => {
            return (
              <div className='grid grid-cols-[1fr_2fr]' key={index}>
                <label htmlFor={field} className='label'>{field.charAt(0).toUpperCase() + field.slice(1)}</label>
                <input type="text" name={field} 
                value={formData[field]} 
                onChange={handleChange}
                className='input rounded-md bg-transparent' />
              </div>
            )
          } ) }
          { error && <span className='text-sm text-red-800'>{error}</span> }
          <button className='btn bg-[#6665EC] self-end rounded-md'>Actualizar</button>
        </form>
      )}

    </div>
  )
}
