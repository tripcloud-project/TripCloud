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
    // type: 'image',
    type: file.contentType?.startsWith('image') ? 'image' : 'file',
    contentType: file.contentType,
    size: file.size,
    parent: currentPrefix || '/',
    // location: currentPrefix ? `My Files > ${currentPrefix}` : 'My Files',
    sido: file.sido,
    sigungu: file.sigungu,
    eupmyeondong: file.eupmyeondong,
    latitude: file.latitude,
    longitude: file.longitude,
    created: file.createdAt,
    modified: file.createdAt,
    taken: file.takenAt,
    preview: file.presignedUrl,
    description: file.description,
    hashtags: file.hashtags?.map(tag => tag.keyword) || [],
  }));

  return [...folderItems, ...fileItems];
};


export default mapApiResponseToItems