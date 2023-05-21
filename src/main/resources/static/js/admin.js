function deleteRealEstate(event) {
    if (!confirm("Вы уверены, что хотите удалить этот объект недвижимости?")) {
        return;
    }
    const id = event.target.dataset.id;
    fetch('/admin/real-estates/' + id, {
        method: 'DELETE'
    }).then(() => {
        window.location.reload();
    });
}

function deleteUser(event) {
    if (!confirm("Вы уверены, что хотите удалить пользователя? Будут удалены все его объявления")) {
        return;
    }
    const id = event.target.dataset.id;
    fetch('/admin/users/' + id, {
        method: 'DELETE'
    }).then(() => {
        window.location.reload();
    });
}