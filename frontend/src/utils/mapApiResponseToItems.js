const mapApiResponseToItems = (data, currentPrefix) => {
  const folderItems = data.directories.map(dir => ({
    id: `${currentPrefix}${dir.name}`,
    name: dir.name,
    type: 'folder',
    size: dir.size,
    parent: currentPrefix || '/',
    // location: currentPrefix ? `My Files > ${currentPrefix}` : 'My Files',
    created: null,
    modified: null,
    count: dir.count,
  }));

  const fileItems = data.files.map(file => ({
    id: file.fileId,
    name: file.name,
    type: 'image',
    size: file.size,
    parent: currentPrefix || '/',
    // location: currentPrefix ? `My Files > ${currentPrefix}` : 'My Files',
    created: file.createdAt,
    modified: file.createdAt,
    taken: file.takenAt,
    preview: file.presignedUrl,
  }));

  return [...folderItems, ...fileItems];
};


export default mapApiResponseToItems