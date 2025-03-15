import React, { useRef, useState } from 'react'
import { FiSend } from 'react-icons/fi'
import { MdOutlineInsertPhoto } from 'react-icons/md'
import { capitalizeFirstLetter } from '../../../libs/utils.js'
import { IoCloseSharp } from "react-icons/io5";
import { request } from '../../../libs/api.js';
import toast from 'react-hot-toast'
import useAuth from '../../../hooks/useAuth.jsx';

const backendUrl = import.meta.env.VITE_BACKEND_URL

export const CreatePost = ( {user, setPosts} ) => {

    const fileRef = useRef(null)
    const [file, setFile] = useState(null)
    const [prevImage, setPrevImage] = useState(null)
    const [text, setText] = useState("")
    const {getCounters} = useAuth()

    const handleCreatePost = async (e) => {
        e.preventDefault()

        const formData = new FormData();
        formData.append("text", text)
        if(file){
            formData.append("image", file)
        }

        await request({
            endpoint: `/publications`,
            method: "POST",
            body: formData,
            multipartFile : true,
            onSuccess: (response) => {
                toast.success("Publicacion realizado con exito")
                setPosts(prev => [response.publication, ...prev])
                getCounters()
            },
            onFailure: (err) => {
                console.log(err)
            }
        })


        setText("")
        setFile(null)

        console.log("create")
    }

    const handleFileChange = (e) => {
        const file = e.target.files[0]
        if(file){
            setFile(file)
            setPrevImage(URL.createObjectURL(file))
        }
    }

    return (
        <div className='form-post'>
            <div className='flex items-center gap-3'>
                {
                user?.image.startsWith("avatar-default") ?
                    <img src={`/avatar-default.jpg`} alt=""
                        className='w-8 h-8 object-cover rounded-full' />
                    :
                    <img src={`${backendUrl}/api/users/avatar/${user?.image}`} alt=""
                        className='w-8 h-8 object-cover rounded-full' />
                }
                <p>{`¿Qué estás pensando, ${ capitalizeFirstLetter(user?.name) }?`}</p>
            </div>
            <form action="" method='POST' encType="multipart/form-data" className='mt-12' onSubmit={handleCreatePost}>
                <input type="text" name='text' className='border-b outline-none border-gray-600 w-full' 
                value={text}
                onChange={(e) => setText(e.target.value)}
                />
                <input type="file" hidden ref={fileRef} onChange={handleFileChange} />

                {file && (
                    <div className='relative  p-5'>
                        <IoCloseSharp
							className='absolute top-2 right-0 text-white bg-gray-800 rounded-full w-5 h-5 cursor-pointer'
							onClick={() => {
								setFile(null);
                                setPrevImage(null)
								fileRef.current.value = null;
							}}
						/>
                        <img src={prevImage} className='w-full mx-auto h-72 object-contain rounded'  />
                    </div>
                ) }

                <div className='flex items-center justify-between mt-4'>
                    <div className='flex items-center gap-3 cursor-pointer'
                    onClick={() => fileRef.current.click()}
                    >
                        <MdOutlineInsertPhoto className='w-6 h-6' />
                        <p>Photo</p>
                    </div>

                    <button type='submit' className='bg-[#6665EC] flex items-center p-2 px-3 rounded-md gap-3 cursor-pointer'>
                        <FiSend className='w-5 h-5 text-white' />
                        <p className='text-white'>Post</p>
                    </button>

                </div>

            </form>
        </div>
    )
}
