function deleteAd(event) {
    const id = event.target.getAttribute('data-id');
    fetch(`/real-estate/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert('Объявление успешно удалено');
            } else {
                alert('Произошла ошибка при удалении объявления');
            }
            window.location.reload();
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}