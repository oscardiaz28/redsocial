import React, { useEffect } from 'react'

export const usePageTitle = ( title ) => {
  useEffect(() => {
    document.title = "Socially | " + title
    
    return () => {
        document.title = "Socially"
    }

  }, [title])
}
